package com.example.splitt.page.mapper;

import com.example.splitt.group.dto.GroupOutputDto;
import com.example.splitt.group.mapper.GroupMapperLite;
import com.example.splitt.group.mapper.GroupMemberMapper;
import com.example.splitt.page.dto.GroupPageDto;
import com.example.splitt.page.dto.GroupPageFullDto;
import com.example.splitt.page.model.GroupPage;
import com.example.splitt.page.model.GroupPageFull;
import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import com.example.splitt.transaction.mapper.TransactionMapper;
import com.example.splitt.user.dto.UserOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GroupPageMapper {

    private final GroupMapperLite groupMapperLite;

    private final GroupMemberMapper memberMapper;

    private final TransactionMapper transactionMapper;

    public GroupPageDto toGroupPageDto(GroupPage groupPage) {
        GroupPageDto groupPageDto = new GroupPageDto();

        List<TransactionOutShortDto> transactionsDto = groupPage.getTransactions().stream()
                .map(transactionMapper::toTransactionOutShortDto)
                .toList();
        groupPageDto.setTransactions(transactionsDto);

        return groupPageDto;
    }

    public GroupPageFullDto toGroupPageFullDto(GroupPageFull groupPage) {
        GroupPageFullDto groupPageDto = new GroupPageFullDto();

        Long currentUserId = groupPage.getUser().getId();
        GroupOutputDto groupDto = groupMapperLite.toGroupOutputDto(groupPage.getGroup());

        List<UserOutputDto> membersDto = groupPage.getGroup().getMembers().stream()
                .map(memberMapper::toUserOutputDto)
                .toList();
        List<TransactionOutShortDto> transactionsDto = groupPage.getTransactions().stream()
                .map(transactionMapper::toTransactionOutShortDto)
                .toList();

        groupPageDto.setCurrentUserId(currentUserId);
        groupPageDto.setGroup(groupDto);
        groupPageDto.setMembers(membersDto);
        groupPageDto.setTransactions(transactionsDto);

        return groupPageDto;
    }
}
