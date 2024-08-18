package com.example.splitt.bill.service.impl;

import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.bill.mapper.BillMapper;
import com.example.splitt.bill.mapper.TransactionMapper;
import com.example.splitt.bill.model.bill.Bill;
import com.example.splitt.bill.model.transaction.Transaction;
import com.example.splitt.bill.repository.BillRepository;
import com.example.splitt.bill.repository.TransactionRepository;
import com.example.splitt.bill.service.BillService;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.SplittValidator;
import com.example.splitt.util.model.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final BillMapper billMapper;

    private final TransactionMapper transactionMapper;

    private final SplittValidator splittValidator;

    @Override
    public List<BillOutDto> findAllByGroupId(Long groupId, Long requesterId, Pageable pageable) {
        GroupMember groupRequester = getGroupMember(groupId, requesterId);
        splittValidator.validateUserIsRegistered(groupRequester.getMember());

        List<Bill> bills = billRepository.findAllByGroupId(groupId, pageable);
        if (bills.isEmpty()) return Collections.emptyList();

        List<Transaction> transactions = getTransactionsForBills(bills);
        transactions.forEach(transaction -> addTransactionToBill(transaction.getBill(), transaction));

        return bills.stream()
                .map(billMapper::toBillOutDto)
                .toList();
    }

    // -------------
    // Repository
    // -------------

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

    private List<Transaction> getTransactionsForBills(List<Bill> bills) {
        List<Long> billIds = bills.stream()
                .map(Bill::getId)
                .toList();
        return transactionRepository.findAllByBill_IdIn(billIds);
    }

    // ------------------
    // Auxiliary Methods
    // ------------------

    private void addTransactionToBill(Bill bill, Transaction transaction) {
        switch (transaction.getType()) {
            case PAYMENT -> {
                if (bill.getPayments() == null) bill.setPayments(new ArrayList<>());
                bill.getPayments().add(transaction);
            }
            case DEBT -> {
                if (bill.getDebts() == null) bill.setDebts(new ArrayList<>());
                bill.getDebts().add(transaction);
            }
            case REPAYMENT_TO -> bill.setRepaymentTo(transaction);
            default -> bill.setRepaymentFrom(transaction);
        }
    }

}
