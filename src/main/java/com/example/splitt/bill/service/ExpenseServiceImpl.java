package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.dto.UserSplitDto;
import com.example.splitt.bill.mapper.BillMapper;
import com.example.splitt.bill.mapper.TransactionMapper;
import com.example.splitt.bill.model.Bill;
import com.example.splitt.bill.model.BillType;
import com.example.splitt.bill.model.Transaction;
import com.example.splitt.bill.model.TransactionType;
import com.example.splitt.bill.repository.BillPaymentRepository;
import com.example.splitt.bill.repository.BillRepository;
import com.example.splitt.bill.repository.TransactionRepository;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.Group;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.group.repository.GroupRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.SplittValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseServiceImpl implements ExpenseService {

    private static final int AMOUNT_CONVERSION_FACTOR = 100;

    private static final String SPLITT_DATE_FORMAT = "yyyy-MM-dd";

    private final BillRepository billRepository;

    private final BillPaymentRepository billPaymentRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final BillMapper billMapper;

    private final TransactionMapper transactionMapper;

    private final SplittValidator splittValidator;

    @Override
    @Transactional
    public ExpenseBalanceOutDto add(ExpenseCreateDto expenseDto) {
        validateBeforeAdd(expenseDto);

        // БД + ВАЛИДАЦИЯ
        // 1) получить requester;
        // 2) получить группу;
        // 3) получить членов группы (GroupMember) — по ID группы и пользователей;
        //    так проверяем, что пользователи состоят в группе

        User requester = getUserById(expenseDto.getRequesterId());
        GroupMemberId groupRequesterId = new GroupMemberId(expenseDto.getRequesterId(),
                expenseDto.getGroupId());
        GroupMember groupRequester = getGroupMemberById(groupRequesterId);
        Group group = groupRequester.getGroup();
        List<GroupMember> groupMembers = getMembersByGroupId(expenseDto.getGroupId());

        // Проверить, что все ID в DTO есть среди членов группы
        validateGroupMembers(groupMembers, expenseDto);

        // ЛОГИКА

        // Создать счет
        // Сохранить счет в БД — получить ID
//        Bill billToSave = buildExpenseBill(expenseDto, requester);
        Bill billToSave = billMapper.toExpenseBill(expenseDto, requester);
        Bill bill = billRepository.save(billToSave);

        Map<Long, User> members = toMembersMap(groupMembers);

        List<Transaction> paymentsToSave = transactionMapper.toTransactions(TransactionType.PAYMENT,
                expenseDto.getPaidBy(), group, bill, members);
        List<Transaction> debtsToSave = transactionMapper.toTransactions(TransactionType.DEBT,
                expenseDto.getDebtShares(), group, bill, members);

//        List<Transaction> paymentsToSave = buildTransactions(TransactionType.PAYMENT,
//                expenseDto.getPaidBy(), group, bill, members);
//                List<Transaction> debtsToSave = buildTransactions(TransactionType.DEBT,
//                expenseDto.getDebtShares(), group, bill, members);

        List<Transaction> payments = transactionRepository.saveAll(paymentsToSave);
        List<Transaction> debts = transactionRepository.saveAll(debtsToSave);

        bill.setPayments(payments);
        bill.setDebts(debts);

        // TODO подготовить к выходу


        return null;
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

    private GroupMember getGroupMemberById(GroupMemberId searchId) {
        return groupMemberRepository.findById(searchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Group Not Found. May be one of the following: " +
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

    // ------------------
    // Auxiliary Methods
    // ------------------

    private int convertAmount(float amountDto) {
        return (int) amountDto * AMOUNT_CONVERSION_FACTOR;
    }

    private Map<Long, User> toMembersMap(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .collect(Collectors.toMap(groupMember -> groupMember.getMember().getId(),
                        GroupMember::getMember));
    }

    // -------------
    // Validation
    // -------------

    private void validateBeforeAdd(ExpenseCreateDto expenseDto) {
        validateTitleNotEmpty(expenseDto.getTitle());
        expenseDto.validatePaidByNotEmpty();
        expenseDto.validateDebtSharesNotEmpty();
        validateExpenseAmount(expenseDto.getAmount(), expenseDto.getPaidBy(), "paidBy");
        validateExpenseAmount(expenseDto.getAmount(), expenseDto.getDebtShares(), "debtShares");
    }

    private void validateTitleNotEmpty(String title) {
        if (splittValidator.isEmpty(title)) {
            throw new CustomValidationException("Expense Title Empty. Please add title.");
        }
    }

    private void validateExpenseAmount(float expenseAmount, List<UserSplitDto> shares, String fieldName) {
        if (!areSharesAddingUp(expenseAmount, shares)) {
            throw new CustomValidationException(String.format("Shares Not Adding Up. " +
                    "Please review shares in the field %s", fieldName));
        }
    }

    private boolean areSharesAddingUp(float amount, List<UserSplitDto> userSplitDtos) {
        int totalAmountExpected = (int) amount * AMOUNT_CONVERSION_FACTOR;
        int totalAmount = userSplitDtos.stream()
                .map(dto -> (int) (dto.getSplitAmount() * AMOUNT_CONVERSION_FACTOR))
                .reduce(0, Integer::sum);

        return totalAmount == totalAmountExpected;
    }

    private void validateGroupMembers(List<GroupMember> groupMembers, ExpenseCreateDto expenseDto) {
        Set<Long> groupMembersIds = groupMembers.stream()
                .map(groupMember -> groupMember.getId().getUserId())
                .collect(Collectors.toSet());

        Set<Long> dtoIds = expenseDto.getPaidBy().stream()
                .map(UserSplitDto::getUserId)
                .collect(Collectors.toSet());

        dtoIds.addAll(expenseDto.getDebtShares().stream()
                .map(UserSplitDto::getUserId)
                .collect(Collectors.toList()));

        if (!groupMembersIds.containsAll(dtoIds)) {
            Set<Long> missingUserIds = dtoIds.stream()
                    .filter(userId -> !groupMembersIds.contains(userId))
                    .collect(Collectors.toSet());

            throw new CustomValidationException("Invalid User IDs. " +
                    "No members in the group with the following IDs: " + missingUserIds);
        }
    }


    // TODO удалить, если не понадобится
    private List<Transaction> buildTransactions(TransactionType transactionType,
                                                List<UserSplitDto> userShares,
                                                Group group,
                                                Bill bill,
                                                Map<Long, User> groupMembers) {
        List<Transaction> transactions = new ArrayList<>();

        for (UserSplitDto userSplitDto : userShares) {
            Transaction transaction = new Transaction();
            transaction.setUser(groupMembers.get(userSplitDto.getUserId()));
            transaction.setAmount(convertAmount(userSplitDto.getSplitAmount()));
            transaction.setType(transactionType);
            transaction.setGroup(group);
            transaction.setBill(bill);
            transactions.add(transaction);
        }

        return transactions;
    }

    // TODO удалить, если не надо
    private Bill buildExpenseBill(ExpenseCreateDto expenseDto, User requester) {
        Bill bill = new Bill();
        bill.setType(BillType.EXPENSE);
        bill.setAddedBy(requester);                                 // TODO отдельно
        bill.setTitle(expenseDto.getTitle());
        bill.setAmount(convertAmount(expenseDto.getAmount()));
        bill.setNote(expenseDto.getNote());
        bill.setDate(LocalDate.parse(expenseDto.getDate(),
                DateTimeFormatter.ofPattern(SPLITT_DATE_FORMAT)));
        bill.setAddedOn(LocalDateTime.now());                       // TODO отдельно
        return bill;
    }
}
