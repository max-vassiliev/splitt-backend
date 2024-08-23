package com.example.splitt.transaction.service.impl;

import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;
import com.example.splitt.transaction.mapper.TransactionMapper;
import com.example.splitt.transaction.mapper.EntryMapper;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.transaction.repository.TransactionRepository;
import com.example.splitt.transaction.repository.EntryRepository;
import com.example.splitt.transaction.service.RepaymentService;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.Group;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.util.SplittValidator;
import com.example.splitt.util.balance.SplittCalculator;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RepaymentServiceImpl implements RepaymentService {

    private final TransactionRepository transactionRepository;

    private final EntryRepository entryRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final TransactionMapper transactionMapper;

    private final EntryMapper entryMapper;

    private final SplittValidator splittValidator;

    private final SplittCalculator splittCalculator;


    @Override
    @Transactional
    public RepaymentOutDto add(RepaymentCreateDto dto) {

        // Validate and retrieve data

        validatePayerNotRecipient(dto);
        
        List<GroupMember> groupMembers = findGroupMembersInCreateDto(dto);
        validateGroupMembersNotEmpty(groupMembers);
        User requester = extractRequesterBeforeAdd(groupMembers, dto.getRequesterId());
        validateRequesterIsRegistered(requester);
        User payer = extractUserFromGroupMembers(groupMembers, dto.getPayerId());
        User recipient = extractUserFromGroupMembers(groupMembers, dto.getRecipientId());
        Group group = groupMembers.get(0).getGroup();

        // Save

        Transaction transactionToSave = transactionMapper.toRepaymentTransaction(dto, requester.getId());
        Transaction savedTransaction = transactionRepository.save(transactionToSave);

        Entry repaymentFrom = entryMapper.toRepayment(payer, group, savedTransaction,
                savedTransaction.getAmount(), true);
        Entry repaymentTo = entryMapper.toRepayment(recipient, group, savedTransaction,
                savedTransaction.getAmount(), false);

        Entry repaymentToSaved = entryRepository.save(repaymentTo);
        Entry repaymentFromSaved = entryRepository.save(repaymentFrom);

        // Prepare for output

        savedTransaction.setRepaymentTo(repaymentToSaved);
        savedTransaction.setRepaymentFrom(repaymentFromSaved);

        List<UserBalanceOutDto> groupBalances = countGroupBalances(group.getId());

        return transactionMapper.toRepaymentOutDto(savedTransaction, groupBalances);
    }

    /*
     -------------
     Repository
     -------------
    */

    private List<GroupMember> findGroupMembersInCreateDto(RepaymentCreateDto dto) {
        Set<Long> usersIds = new HashSet<>();
        usersIds.add(dto.getRequesterId());
        usersIds.add(dto.getPayerId());
        usersIds.add(dto.getRecipientId());

        List<GroupMemberId> searchIds = usersIds.stream()
                .map(userId -> new GroupMemberId(userId, dto.getGroupId()))
                .toList();

        return groupMemberRepository.findByIdIn(searchIds);
    }

    private List<UserBalance> getUserBalancesInGroup(Long groupId) {
        return entryRepository.getUserBalancesInGroup(groupId);
    }

    /*
     ------------------
     Auxiliary Methods
     ------------------
    */

    private List<UserBalanceOutDto> countGroupBalances(Long groupId) {
        List<UserBalance> userBalances = getUserBalancesInGroup(groupId);
        return splittCalculator.calculateBalance(userBalances);
    }

    private User extractUserFromGroupMembers(List<GroupMember> groupMembers, Long userId) {
        return groupMembers.stream()
                .map(GroupMember::getMember)
                .filter(member -> userId.equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User Not Found. User with ID=%s does not " +
                                "have access to the group or does not exist.", userId),
                        User.class
                ));
    }

    private User extractRequesterBeforeAdd(List<GroupMember> groupMembers,
                                           Long requesterId) {
        User requester = extractUserFromGroupMembers(groupMembers, requesterId);
        validateRequesterIsRegistered(requester);
        return requester;
    }

    /*
     -------------
     Validation
     -------------
    */

    private void validatePayerNotRecipient(RepaymentCreateDto dto) {
        if (dto.isPayerRecipient()) {
            throw new CustomValidationException("Repayment Not Valid. " +
                    "Users cannot record repayments to themselves.");
        }
    }
    
    private void validateRequesterIsRegistered(User requester) {
        if (!splittValidator.isUserRegistered(requester)) {
            throw new CustomValidationException("User Not Registered. " +
                    "Only registered users can manage repayments.");
        }
    }

    private void validateGroupMembersNotEmpty(List<GroupMember> groupMembers) {
        if (groupMembers.isEmpty()) {
            throw new EntityNotFoundException("Group or Users Not Found. " +
                    "No match for group and users with provided IDs.",
                    GroupMember.class);
        }
    }
}
