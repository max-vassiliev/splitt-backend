package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.dto.UserSplitDto;
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

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseServiceImpl implements ExpenseService {

    private static final int AMOUNT_CONVERSION_FACTOR = 100;

    private final BillRepository billRepository;

    private final BillPaymentRepository billPaymentRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

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
        List<GroupMember> groupMembers = getMembersByGroupId(expenseDto.getGroupId());

        // ВАЛИДАЦИЯ
        // Проверить, что все ID в DTO есть среди членов группы
        validateGroupMembers(groupMembers, expenseDto);

        // ЛОГИКА

        // Создать счет
        // Сохранить счет в БД — получить ID

        // Создать транзакции
        // Сохранить транзакции в БД — получить ID

        //
        return null;
    }

    private void validateGroupMembers(List<GroupMember> groupMembers, ExpenseCreateDto expenseDto) {
//        Set<Long> dtoIds =
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

    private void validateExpenseAmount(float expenseAmount,
                                       List<UserSplitDto> shares,
                                       String fieldName) {
        if (!areSharesAddingUp(expenseAmount, shares)) {
            throw new CustomValidationException(String.format("Shares Not Adding Up. " +
                    "Please review shares in the field %s", fieldName));
        }
    }

    private void validateTitleNotEmpty(String title) {
        if (splittValidator.isEmpty(title)) {
            throw new CustomValidationException("Expense Title Empty. Please add title.");
        }
    }

    private boolean areSharesAddingUp(float amount, List<UserSplitDto> userSplitDtos) {
        int totalAmountExpected = (int) amount * AMOUNT_CONVERSION_FACTOR;
        int totalAmount = userSplitDtos.stream()
                .map(dto -> (int) (dto.getSplitAmount() * AMOUNT_CONVERSION_FACTOR))
                .reduce(0, Integer::sum);

        return totalAmount == totalAmountExpected;
    }
}
