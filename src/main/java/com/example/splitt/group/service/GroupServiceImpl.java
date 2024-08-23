package com.example.splitt.group.service;

import com.example.splitt.group.dto.GroupCreateDto;
import com.example.splitt.group.dto.GroupOutputFullDto;
import com.example.splitt.group.dto.GroupOutputShortDto;
import com.example.splitt.group.dto.GroupUpdateDto;
import com.example.splitt.group.dto.GroupUpdateMembersDto;
import com.example.splitt.group.dto.page.GroupPageFullDto;
import com.example.splitt.group.mapper.GroupPageMapper;
import com.example.splitt.group.model.Group;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.model.MemberStatus;
import com.example.splitt.group.model.page.GroupPageFull;
import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.transaction.model.entry.EntryType;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.transaction.TransactionType;
import com.example.splitt.transaction.repository.EntryRepository;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.dto.member.MemberInputDto;
import com.example.splitt.group.dto.member.CurrentMemberInputDto;
import com.example.splitt.group.dto.member.NewMemberInputDto;
import com.example.splitt.group.mapper.GroupMapper;
import com.example.splitt.group.mapper.GroupMapperLite;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.group.repository.GroupRepository;
import com.example.splitt.transaction.repository.TransactionRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.SplittValidator;
import com.example.splitt.util.balance.SplittCalculator;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GroupServiceImpl implements GroupService {

    private static final int PROCESS_MEMBERS_LIMIT = 20;

    private static final int GROUP_SIZE_LIMIT = 100;

    private final GroupRepository groupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final EntryRepository entryRepository;

    private final SplittValidator splittValidator;

    private final SplittCalculator splittCalculator;

    private final GroupMapper groupMapper;

    private final GroupMapperLite groupMapperLite;

    private final GroupPageMapper groupPageMapper;


    @Override
    @Transactional
    public GroupOutputFullDto findById(Long groupId, Long requesterId) {
        GroupMemberId searchId = new GroupMemberId(requesterId, groupId);
        GroupMember groupRequester = getGroupMemberById(searchId);
        Group group = groupRequester.getGroup();
        updateLastViewedGroup(groupRequester.getMember(), group);

        List<GroupMember> foundGroupMembers = getMembersByGroupId(groupId);
        populateGroup(group, foundGroupMembers);

        List<UserBalanceOutDto> groupBalances = countGroupBalances(groupId);

        return groupMapper.toGroupOutputFullDto(group, groupBalances);
    }

    @Override
    @Transactional
    public GroupPageFullDto getGroupFullPageById(Long groupId, Long requesterId, Pageable pageable) {
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
        setUserTransactionBalances(groupPage, groupPageDto);

        return groupPageDto;
    }

    @Override
    public List<GroupOutputShortDto> findAllByUserId(Long userId) {
        getUserById(userId);
        List<GroupMember> userGroups = getGroupsByUserId(userId);
        if (userGroups.isEmpty()) return Collections.emptyList();

        return userGroups.stream()
                .map(GroupMember::getGroup)
                .map(groupMapperLite::toGroupOutputShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroupOutputFullDto create(Long requesterId, GroupCreateDto dto) {
        User requester = getUserById(requesterId);
        validateBeforeCreate(requester, dto);

        List<User> members = processGroupMembers(dto.getMembers());

        Group group = new Group(dto.getTitle());
        Group savedGroup = saveNewGroup(group);

        List<GroupMember> groupMembers = createGroupMembers(savedGroup, requester, members);
        List<GroupMember> groupMembersSaved = saveGroupMembers(groupMembers);
        Set<User> savedMembers = groupMembersSaved.stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toCollection(HashSet::new));
        savedGroup.setMembers(savedMembers);

        return groupMapper.toGroupOutputFullDto(savedGroup);
    }

    @Override
    @Transactional
    public GroupOutputShortDto updateProperties(GroupUpdateDto dto) {
        validateUpdatePropertiesNotNull(dto);

        GroupMemberId searchId = new GroupMemberId(dto.getRequesterId(), dto.getGroupId());
        GroupMember groupRequester = getGroupMemberById(searchId);
        validateRequesterIsRegistered(groupRequester.getMember());

        Group group = groupRequester.getGroup();
        updateGroupProperties(group, dto);

        groupRepository.flush();

        return groupMapperLite.toGroupOutputShortDto(group);
    }

    @Override
    @Transactional
    public GroupOutputFullDto updateMembers(GroupUpdateMembersDto dto) {
        validateBeforeUpdateMembers(dto);

        GroupMemberId searchId = new GroupMemberId(dto.getRequesterId(), dto.getGroupId());
        GroupMember groupRequester = getGroupMemberById(searchId);
        validateRequesterIsRegistered(groupRequester.getMember());

        Group group = groupRequester.getGroup();
        List<GroupMember> groupMembers = getMembersByGroupId(group.getId());
        validateMembersNumber(groupMembers.size(), dto.getNewMembers());

        updateCurrentGroupMembers(groupMembers, group, dto.getCurrentMembers());
        addNewMembersToGroup(group, dto.getNewMembers());

        userRepository.flush();
        groupMemberRepository.flush();

        return groupMapper.toGroupOutputFullDto(group);
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

    private List<GroupMember> getGroupsByUserId(Long userId) {
        return groupMemberRepository.findByIdUserId(userId);
    }

    private List<GroupMember> getMembersByGroupId(Long groupId) {
        return groupMemberRepository.findByIdGroupId(groupId);
    }

    private Group saveNewGroup(Group group) {
        return groupRepository.save(group);
    }

    private List<GroupMember> saveGroupMembers(List<GroupMember> groupMembers) {
        return groupMemberRepository.saveAll(groupMembers);
    }

    private User getUserById(Long requesterId) {
        return userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found. Requested ID: " + requesterId,
                        User.class
                ));
    }

    private List<User> getUsersByEmails(List<String> emails) {
        return userRepository.findByEmails(emails);
    }

    private List<User> saveNewUsers(List<User> newUsers) {
        return userRepository.saveAll(newUsers);
    }

    private List<UserBalance> getUserBalancesInGroup(Long groupId) {
        return entryRepository.getUserBalancesInGroup(groupId);
    }

    private void updateLastViewedGroup(User requester, Group group) {

        if (requester.getLastViewedGroup() != null &&
                Objects.equals(requester.getLastViewedGroup().getId(), group.getId())) {
            return;
        }
        requester.setLastViewedGroup(group);
        userRepository.flush();
    }

    // ------------------
    // Auxiliary Methods
    // ------------------

    private void setUserTransactionBalances(GroupPageFull groupPage,
                                             GroupPageFullDto groupPageDto) {
        List<Entry> userEntries = getTransactionEntriesForUser(groupPage.getUser().getId(),
                groupPage.getTransactions());
        if (userEntries.isEmpty()) return;

        Map<Long, Integer> userTransactionBalances = new HashMap<>();

        userEntries.forEach(entry -> {
            Long transactionId = entry.getTransaction().getId();
            int entryAmount = retrieveEntryAmount(entry);
            userTransactionBalances.merge(transactionId, entryAmount, Integer::sum);
        });

        groupPageDto.getTransactions().forEach(transaction -> {
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

    private List<User> processGroupMembers(List<NewMemberInputDto> memberDtos) {
        if (memberDtos == null || memberDtos.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> members = extractExistingUsers(memberDtos);

        if (members.size() == memberDtos.size()) {
            return members;
        }

        List<User> newUsers = extractNewUsers(memberDtos, members);
        List<User> newUsersSaved = saveNewUsers(newUsers);
        members.addAll(newUsersSaved);
        
        return members;
    }
    
    private List<User> extractExistingUsers(List<NewMemberInputDto> membersDtos) {
        List<String> membersEmails = membersDtos.stream()
                .map(MemberInputDto::getEmail)
                .filter(splittValidator.isNotBlankString())
                .collect(Collectors.toList());

        return getUsersByEmails(membersEmails);
    }
    
    private List<User> extractNewUsers(List<NewMemberInputDto> memberDtos, List<User> members) {
        List<User> newUsers = new ArrayList<>();

        HashSet<String> foundMembersEmails = members.stream()
                .map(User::getEmail)
                .collect(Collectors.toCollection(HashSet::new));

        for (MemberInputDto member : memberDtos) {
            if (member.getEmail() != null && foundMembersEmails.contains(member.getEmail())) {
                continue;
            }
            validateNewMemberFields(member);
            User newUser = new User(member.getName());
            if (!splittValidator.isEmpty(member.getEmail())) {
                newUser.setEmail(member.getEmail());
            }
            newUsers.add(newUser);
        }

        return newUsers;
    }

    private static List<GroupMember> createGroupMembers(Group savedGroup,
                                                        User requester,
                                                        List<User> otherMembers) {
        List<GroupMember> groupMembers = new ArrayList<>();

        if (requester != null) {
            GroupMember groupFounder = new GroupMember(
                    new GroupMemberId(requester.getId(), savedGroup.getId()),
                    requester,
                    savedGroup,
                    MemberStatus.CONFIRMED
            );
            groupMembers.add(groupFounder);
        }

        if (otherMembers.isEmpty()) {
            return groupMembers;
        }

        List<GroupMember> otherGroupMembers = otherMembers.stream()
                .map(member -> new GroupMember(
                        new GroupMemberId(member.getId(), savedGroup.getId()),
                        member,
                        savedGroup,
                        MemberStatus.CONFIRMED))
                .toList();
        groupMembers.addAll(otherGroupMembers);

        return groupMembers;
    }

    private void updateGroupProperties(Group group, GroupUpdateDto dto) {
        if (dto.getTitle() == null) return;
        validateGroupTitleNotEmpty(dto.getTitle());
        group.setTitle(dto.getTitle());
    }


    private void updateCurrentGroupMembers(List<GroupMember> groupMembers,
                                           Group group,
                                           List<CurrentMemberInputDto> membersToUpdateDto) {
        if (membersToUpdateDto == null || membersToUpdateDto.isEmpty()) {
            populateGroup(group, groupMembers);
            return;
        }

        Map<Long, GroupMember> groupMembersToUpdate = groupMembers.stream()
                .collect(Collectors.toMap(gm -> gm.getMember().getId(), groupMember -> groupMember));

        Map<Long, User> members = groupMembers.stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toMap(User::getId, user -> user));

        for (CurrentMemberInputDto dto : membersToUpdateDto) {
            if (members.containsKey(dto.getId())) {
                User member = members.get(dto.getId());
                GroupMember groupMember = groupMembersToUpdate.get(dto.getId());
                updateMemberFields(member, groupMember, dto);
            } else {
                throw new EntityNotFoundException(
                        String.format("User Not Found. User with ID %d not found in the group.", dto.getId()),
                        User.class
                );
            }
        }

        group.setMembers(new LinkedHashSet<>(members.values()));
    }

    private void updateMemberFields(User member, GroupMember groupMember, CurrentMemberInputDto memberUpdate) {
        validateBeforeUpdateMemberNotRegistered(member);

        if (memberUpdate.getName() != null && !splittValidator.isEmpty(memberUpdate.getName())) {
            member.setName(memberUpdate.getName());
        }
        if (memberUpdate.getEmail() != null) {
            String email = splittValidator.isEmpty(memberUpdate.getEmail())
                    ? null
                    : memberUpdate.getEmail();
            member.setEmail(email);
        }
        groupMember.setMember(member);
    }

    private void addNewMembersToGroup(Group group, List<NewMemberInputDto> membersDtos) {
        List<User> newMembers = processGroupMembers(membersDtos);

        List<GroupMember> newGroupMembers = createGroupMembers(group, null, newMembers);
        List<GroupMember> newGroupMembersSaved = saveGroupMembers(newGroupMembers);

        Set<User> newMembersSaved = newGroupMembersSaved.stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        group.getMembers().addAll(newMembersSaved);
    }

    private int retrieveEntryAmount(Entry entry) {
        return (EntryType.DEBT.equals(entry.getType()) ||
                EntryType.REPAYMENT_FROM.equals(entry.getType()))
                ? -entry.getAmount()
                : entry.getAmount();
    }

    // -------------
    // Validation
    // -------------

    private void validateBeforeCreate(User requester, GroupCreateDto groupDto) {
        validateRequesterIsRegistered(requester);
        validateGroupTitleNotEmpty(groupDto.getTitle());
        validateProcessMembersListNotLong(groupDto.getMembers());
    }

    private void validateBeforeUpdateMembers(GroupUpdateMembersDto dto) {
        dto.validateMembersListsNotEmpty();

        List<MemberInputDto> allMembersToProcess = new ArrayList<>();

        if (dto.getCurrentMembers() != null && !dto.getCurrentMembers().isEmpty()) {
            allMembersToProcess.addAll(dto.getCurrentMembers());
        }
        if (dto.getNewMembers() != null && !dto.getNewMembers().isEmpty()) {
            allMembersToProcess.addAll(dto.getNewMembers());
        }

        validateBeforeUpdateMembersNamesNotBlank(allMembersToProcess);
        validateProcessMembersListNotLong(allMembersToProcess);
    }

    private void validateProcessMembersListNotLong(List<? extends MemberInputDto> members) {
        if (members == null || members.isEmpty()) return;
        if (members.size() > PROCESS_MEMBERS_LIMIT) {
            throw new CustomValidationException("Too Many Members. " +
                    "We can only add and update " + PROCESS_MEMBERS_LIMIT + " members at a time. " +
                    "Try adding or updating the rest later.");
        }
    }

    private void validateGroupTitleNotEmpty(String title) {
        if (splittValidator.isEmpty(title)) {
            throw new CustomValidationException("Group Title Empty. " +
                    "Please add a title for this group.");
        }
    }

    private void validateRequesterIsRegistered(User requester) {
        if (!splittValidator.isUserRegistered(requester)) {
            throw new CustomValidationException("User Not Registered. " +
                    "Only registered users can manage groups.");
        }
    }

    private void validateUpdatePropertiesNotNull(GroupUpdateDto dto) {
        if (dto.getTitle() == null) {
            throw new CustomValidationException("Nothing to Update. " +
                    "Please add group properties that you would like to update.");
        }
    }

    private void validateBeforeUpdateMemberNotRegistered(User member) {
        if (splittValidator.isUserRegistered(member)) {
            throw new CustomValidationException(String.format("User Data Cannot Be Changed. " +
                            "User %d is a registered user. Other users cannot update their data.",
                    member.getId()));
        }
    }

    private void validateBeforeUpdateMembersNamesNotBlank(List<? extends MemberInputDto> membersToProcess) {
        List<String> blankNames = membersToProcess.stream()
                .map(MemberInputDto::getName)
                .filter(splittValidator.isBlankString())
                .toList();

        if (!blankNames.isEmpty()) {
            throw new CustomValidationException("User Name Empty. User names cannot be empty.");
        }
    }

    private void validateNewMemberFields(MemberInputDto newMemberDto) {
        if (newMemberDto.getName() == null || splittValidator.isEmpty(newMemberDto.getName())) {
            throw new CustomValidationException("Name Empty. " +
                    "Please add names for new group members");
        }
    }

    private void validateMembersNumber(int groupSize, List<? extends MemberInputDto> newMembers) {
        if (newMembers == null || newMembers.isEmpty()) return;
        if (groupSize + newMembers.size() > GROUP_SIZE_LIMIT) {
            throw new CustomValidationException(String.format("Group Size Limit Exceeded. " +
                            "Looks like you're about to reach the limit of %d users per group. " +
                            "You can add %d more users to this group. You were planning to add %d.",
                    GROUP_SIZE_LIMIT, GROUP_SIZE_LIMIT - groupSize, newMembers.size()));
        }
    }
}
