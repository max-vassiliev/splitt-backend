package com.example.splitt.page.service;

import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.Group;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.page.dto.GroupPageDto;
import com.example.splitt.page.dto.GroupPageFullDto;
import com.example.splitt.page.mapper.GroupPageMapper;
import com.example.splitt.page.model.GroupPage;
import com.example.splitt.page.model.GroupPageFull;
import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import com.example.splitt.transaction.mapper.TransactionMapper;
import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.transaction.model.entry.EntryType;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.transaction.TransactionType;
import com.example.splitt.transaction.repository.EntryRepository;
import com.example.splitt.transaction.repository.TransactionRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.balance.SplittCalculator;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GroupPageServiceImpl implements GroupPageService {

    private final GroupMemberRepository groupMemberRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final EntryRepository entryRepository;

    private final SplittCalculator splittCalculator;

    private final GroupPageMapper groupPageMapper;

    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public GroupPageFullDto getByGroupIdFull(Long groupId, Long requesterId, Pageable pageable) {
        GroupMemberId searchId = new GroupMemberId(requesterId, groupId);
        GroupMember groupRequester = getGroupMemberById(searchId);
        User requester = groupRequester.getMember();
        Group group = groupRequester.getGroup();
        updateLastViewedGroup(requester, group);

        List<GroupMember> foundGroupMembers = getMembersByGroupId(groupId);
        populateGroup(group, foundGroupMembers);
        List<Transaction> transactions = getTransactionsByGroupId(groupId, pageable);

        GroupPageFull groupPage = new GroupPageFull(requester, group, transactions);
        GroupPageFullDto groupPageDto = groupPageMapper.toGroupPageFullDto(groupPage);

        List<UserBalanceOutDto> groupBalancesDto = countGroupBalances(groupId);
        groupPageDto.setBalances(groupBalancesDto);
        setUserTransactionBalances(groupPage.getTransactions(), groupPageDto.getTransactions(), requesterId);

        return groupPageDto;
    }

    @Override
    public GroupPageDto getByGroupId(Long groupId, Long requesterId, Pageable pageable) {
        GroupMemberId searchId = new GroupMemberId(requesterId, groupId);
        getGroupMemberById(searchId);

        List<Transaction> transactions = getTransactionsByGroupId(groupId, pageable);

        GroupPage groupPage = new GroupPage(transactions);
        GroupPageDto groupPageDto = groupPageMapper.toGroupPageDto(groupPage);

        List<UserBalanceOutDto> groupBalancesDto = countGroupBalances(groupId);
        groupPageDto.setBalances(groupBalancesDto);
        setUserTransactionBalances(groupPage.getTransactions(), groupPageDto.getTransactions(), requesterId);

        return groupPageDto;
    }

    @Override
    public List<TransactionOutShortDto> getByGroupIdTransactions(Long groupId,
                                                                 Long requesterId,
                                                                 Pageable pageable) {
        GroupMemberId searchId = new GroupMemberId(requesterId, groupId);
        getGroupMemberById(searchId);

        List<Transaction> transactions = getTransactionsByGroupId(groupId, pageable);
        List<TransactionOutShortDto> transactionsDto = transactions.stream()
                .map(transactionMapper::toTransactionOutShortDto)
                .toList();
        setUserTransactionBalances(transactions, transactionsDto, requesterId);

        return transactionsDto;
    }

    // -------------
    // Repository
    // -------------

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

    private void updateLastViewedGroup(User requester, Group group) {

        if (requester.getLastViewedGroup() != null &&
                Objects.equals(requester.getLastViewedGroup().getId(), group.getId())) {
            return;
        }
        requester.setLastViewedGroup(group);
        userRepository.flush();
    }

    private List<Transaction> getTransactionsByGroupId(Long groupId, Pageable pageable) {
        List<Transaction> transactions = transactionRepository.findAllByGroupId(groupId, pageable);
        if (transactions.isEmpty()) return Collections.emptyList();

        List<Entry> entries = getEntriesForTransactions(transactions);
        entries.forEach(entry -> addEntryToTransaction(entry.getTransaction(), entry));

        return transactions;
    }

    private List<Entry> getEntriesForTransactions(List<Transaction> transactions) {
        List<Long> transactionIds = transactions.stream()
                .map(Transaction::getId)
                .toList();
        return entryRepository.findAllByTransaction_IdIn(transactionIds);
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

    private void populateGroup(Group group, List<GroupMember> groupMembers) {
        Set<User> members = groupMembers.stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        group.setMembers(members);
    }

    private void setUserTransactionBalances(List<Transaction> transactions,
                                            List<TransactionOutShortDto> transactionsOutDto,
                                            Long requesterId) {
        List<Entry> userEntries = getTransactionEntriesForUser(requesterId, transactions);
        if (userEntries.isEmpty()) return;

        Map<Long, Integer> userTransactionBalances = new HashMap<>();

        userEntries.forEach(entry -> {
            Long transactionId = entry.getTransaction().getId();
            int entryAmount = retrieveEntryAmount(entry);
            userTransactionBalances.merge(transactionId, entryAmount, Integer::sum);
        });

        transactionsOutDto.forEach(transaction -> {
            Integer userBalance = userTransactionBalances.get(transaction.getId());
            // Note: userBalance can be null to indicate the user was not involved in the transaction
            transaction.setCurrentUserBalance(userBalance);
        });
    }

    private List<Entry> getTransactionEntriesForUser(long userId, List<Transaction> transactions) {
        return transactions.stream()
                .flatMap(transaction -> {
                    if (transaction.getType() == TransactionType.EXPENSE) {
                        return Stream.concat(
                                transaction.getPayments().stream(),
                                transaction.getSplitts().stream()
                        );
                    } else if (transaction.getType() == TransactionType.REPAYMENT) {
                        return Stream.of(
                                transaction.getRepaymentTo(),
                                transaction.getRepaymentFrom()
                        ).flatMap(Stream::ofNullable);
                    } else {
                        return Stream.empty();
                    }
                })
                .filter(entry -> entry.getUserId() == userId)
                .toList();
    }

    private void addEntryToTransaction(Transaction transaction, Entry entry) {
        switch (entry.getType()) {
            case PAYMENT -> {
                if (transaction.getPayments() == null) transaction.setPayments(new ArrayList<>());
                transaction.getPayments().add(entry);
            }
            case DEBT -> {
                if (transaction.getSplitts() == null) transaction.setSplitts(new ArrayList<>());
                transaction.getSplitts().add(entry);
            }
            case REPAYMENT_TO -> transaction.setRepaymentTo(entry);
            default -> transaction.setRepaymentFrom(entry);
        }
    }

    private int retrieveEntryAmount(Entry entry) {
        return (EntryType.DEBT.equals(entry.getType()) ||
                EntryType.REPAYMENT_FROM.equals(entry.getType()))
                ? -entry.getAmount()
                : entry.getAmount();
    }
}
