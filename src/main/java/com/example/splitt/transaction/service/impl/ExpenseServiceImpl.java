package com.example.splitt.transaction.service.impl;

import com.example.splitt.transaction.dto.expense.ExpenseOutDto;
import com.example.splitt.transaction.dto.expense.ExpenseCreateDto;
import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.mapper.TransactionMapper;
import com.example.splitt.util.balance.SplittCalculator;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplittDto;
import com.example.splitt.transaction.mapper.EntryMapper;
import com.example.splitt.transaction.model.entry.EntryType;
import com.example.splitt.transaction.repository.TransactionRepository;
import com.example.splitt.transaction.repository.EntryRepository;
import com.example.splitt.transaction.service.ExpenseService;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.Group;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.validation.SplittValidator;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseServiceImpl implements ExpenseService {

    private final TransactionRepository transactionRepository;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final TransactionMapper transactionMapper;

    private final EntryMapper entryMapper;

    private final SplittValidator splittValidator;

    private final SplittCalculator splittCalculator;


    @Override
    @Transactional
    public ExpenseOutDto add(ExpenseCreateDto dto) {

        // Validate and retrieve data

        validateBeforeAdd(dto);

        User requester = getUserById(dto.getRequesterId());
        validateRequesterIsRegistered(requester);

        GroupMember groupRequester = getGroupMember(dto.getGroupId(), dto.getRequesterId());
        Group group = groupRequester.getGroup();
        List<GroupMember> groupMembers = getMembersByGroupId(dto.getGroupId());
        validateGroupMembers(groupMembers, dto);

        // Save data

        Transaction transactionToSave = transactionMapper.toExpenseTransaction(dto, requester.getId());
        Transaction transaction = transactionRepository.save(transactionToSave);

        List<Entry> paymentsToSave = entryMapper.toEntries(EntryType.PAYMENT,
                dto.getPaidBy(), group, transaction);
        List<Entry> splittsToSave = entryMapper.toEntries(EntryType.DEBT,
                dto.getSplitts(), group, transaction);

        List<Entry> payments = entryRepository.saveAll(paymentsToSave);
        List<Entry> splitts = entryRepository.saveAll(splittsToSave);

        // Prepare for output

        transaction.setPayments(payments);
        transaction.setSplitts(splitts);

        List<UserBalanceOutDto> groupBalances = countGroupBalances(group.getId());

        return transactionMapper.toExpenseOutDto(transaction, groupBalances);
    }


    // -------------
    // Repository
    // -------------

    private User getUserById(Long requesterId) {
        return userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found. Requested ID: " + requesterId,
                        User.class
                ));
    }

    private GroupMember getGroupMember(Long groupId, Long userId) {
        GroupMemberId searchId = new GroupMemberId(groupId, userId);
        return groupMemberRepository.findById(searchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Group or User Not Found. May be one of the following: " +
                                        "a) group with ID=%d does not exist; " +
                                        "b) user with ID=%d does not exist " +
                                        "or does not have access to the group",
                                searchId.getGroupId(),
                                searchId.getUserId()),
                        GroupMember.class
                ));
    }

    private List<GroupMember> getMembersByGroupId(Long groupId) {
        return groupMemberRepository.findByIdGroupId(groupId);
    }

    private List<UserBalance> getUserBalancesInGroup(Long groupId) {
        return entryRepository.getUserBalancesInGroup(groupId);
    }

    // ------------------
    // Auxiliary Methods
    // ------------------

    private List<UserBalanceOutDto> countGroupBalances(Long groupId) {
        List<UserBalance> userBalances = getUserBalancesInGroup(groupId);
        return splittCalculator.calculateBalance(userBalances);
    }

    // -------------
    // Validation
    // -------------

    private void validateBeforeAdd(ExpenseCreateDto expenseDto) {
        expenseDto.validatePaidByNotEmpty();
        expenseDto.validateDebtSharesNotEmpty();
        validateExpenseAmount(expenseDto.getAmount(), expenseDto.getPaidBy(), "paidBy");
        validateExpenseAmount(expenseDto.getAmount(), expenseDto.getSplitts(), "debtShares");
    }

    private void validateExpenseAmount(int expenseAmount, List<UserSplittDto> shares, String fieldName) {
        if (!areSharesAddingUp(expenseAmount, shares)) {
            throw new CustomValidationException(String.format("Shares Not Adding Up. " +
                    "Please review shares in the field '%s'.", fieldName));
        }
    }

    private void validateRequesterIsRegistered(User requester) {
        if (!splittValidator.isUserRegistered(requester)) {
            throw new CustomValidationException("User Not Registered. " +
                    "Only registered users can manage expenses.");
        }
    }

    private boolean areSharesAddingUp(int amount, List<UserSplittDto> userSplittDtos) {
        int userSplittSum = userSplittDtos.stream()
                .map(UserSplittDto::getAmount)
                .reduce(0, Integer::sum);

        return userSplittSum == amount;
    }

    private void validateGroupMembers(List<GroupMember> groupMembers, ExpenseCreateDto expenseDto) {
        Set<Long> groupMembersIds = groupMembers.stream()
                .map(groupMember -> groupMember.getId().getUserId())
                .collect(Collectors.toSet());

        Set<Long> dtoIds = expenseDto.getPaidBy().stream()
                .map(UserSplittDto::getUserId)
                .collect(Collectors.toSet());

        dtoIds.addAll(expenseDto.getSplitts().stream()
                .map(UserSplittDto::getUserId)
                .toList());

        if (!groupMembersIds.containsAll(dtoIds)) {
            Set<Long> missingUserIds = dtoIds.stream()
                    .filter(userId -> !groupMembersIds.contains(userId))
                    .collect(Collectors.toSet());

            throw new CustomValidationException("Invalid User IDs. " +
                    "No members in the group with the following IDs: " + missingUserIds);
        }
    }
}
