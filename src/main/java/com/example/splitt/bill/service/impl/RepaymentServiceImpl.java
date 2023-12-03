package com.example.splitt.bill.service.impl;

import com.example.splitt.bill.dto.repayment.RepaymentCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentOutDto;
import com.example.splitt.bill.mapper.BillMapper;
import com.example.splitt.bill.mapper.TransactionMapper;
import com.example.splitt.bill.model.bill.Bill;
import com.example.splitt.bill.model.transaction.Transaction;
import com.example.splitt.bill.repository.BillRepository;
import com.example.splitt.bill.repository.TransactionRepository;
import com.example.splitt.bill.service.RepaymentService;
import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import com.example.splitt.group.repository.GroupMemberRepository;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.SplittValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RepaymentServiceImpl implements RepaymentService {

    private final BillRepository billRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final BillMapper billMapper;

    private final TransactionMapper transactionMapper;

    private final SplittValidator splittValidator;


    @Override
    @Transactional
    public RepaymentOutDto add(RepaymentCreateDto dto) {

        // Validate and retrieve data
        validatePayerNotRecipient(dto);
        
//        User requester2 = getUserById(dto.getRequesterId()); // TODO удалить потом
//        validateRequesterIsRegistered(requester2);
        
        List<GroupMember> groupMembers = findGroupMembersInCreateDto(dto);
        validateGroupMembersNotEmpty(groupMembers);
        User requester = extractRequesterBeforeAdd(groupMembers, dto.getRequesterId());
        validateRequesterIsRegistered(requester);
        User payer = extractUserFromGroupMembers(groupMembers, dto.getPayerId());
        User recipient = extractUserFromGroupMembers(groupMembers, dto.getRecipientId());

        // Save

        Bill billToSave = billMapper.toRepaymentBill(dto, requester);
        Bill savedBill = billRepository.save(billToSave);

        Transaction transactionToSave = transactionMapper.toRepayment(payer, recipient,
                groupMembers.get(0).getGroup(), savedBill, savedBill.getAmount());
        Transaction savedTransaction = transactionRepository.save(transactionToSave);

        // Prepare for output
        savedBill.setRepayment(savedTransaction);

        // TODO написать маппер
        return null;
    }

    private User extractRequesterBeforeAdd(List<GroupMember> groupMembers,
                                           Long requesterId) {
        User requester = extractUserFromGroupMembers(groupMembers, requesterId);
        validateRequesterIsRegistered(requester);
        return requester;
    }

    private void validatePayerAndRecipientBeforeAdd(List<GroupMember> groupMembers,
                                                    RepaymentCreateDto dto) {
        extractUserFromGroupMembers(groupMembers, dto.getPayerId());
    }

    private void validateRequesterBeforeAdd(List<GroupMember> groupMembers,
                                            RepaymentCreateDto dto) {
        User requester = extractUserFromGroupMembers(groupMembers, dto.getRequesterId());
//                groupMembers.stream()
//                .map(GroupMember::getMember)
//                .filter(member -> member.getId().equals(dto.getRequesterId()))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format("User Not Found. No user found with ID=%s", dto.getRequesterId()),
//                        User.class
//                ));

        if (!splittValidator.isUserRegistered(requester)) {
            throw new CustomValidationException("User Not Registered. " +
                    "Only registered users can manage repayments.");
        }
    }

    private User extractUserFromGroupMembers(List<GroupMember> groupMembers, Long userId) {
        return groupMembers.stream()
                .map(GroupMember::getMember)
                .filter(member -> userId.equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User Not Found. No user found with ID=%s", userId),
                        User.class
                ));
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

    private List<GroupMember> findGroupMembersInCreateDto(RepaymentCreateDto dto) {
        Set<Long> usersIds = new HashSet<>(Set.of(
                dto.getRequesterId(),
                dto.getPayerId(),
                dto.getRecipientId()
        ));

        List<GroupMemberId> searchIds = usersIds.stream()
                .map(userId -> new GroupMemberId(userId, dto.getGroupId()))
                .toList();

        return groupMemberRepository.findByIdIn(searchIds);
    }

    // -------------
    // Validation
    // -------------

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
