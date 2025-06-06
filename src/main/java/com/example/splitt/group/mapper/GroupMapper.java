package com.example.splitt.group.mapper;

import com.example.splitt.group.dto.output.GroupOutputFullDto;
import com.example.splitt.group.model.Group;
import com.example.splitt.user.dto.UserOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GroupMapper {

    private final GroupMapperLite groupMapperLite;

    private final GroupMemberMapper memberMapper;


    public GroupOutputFullDto toGroupOutputFullDto(Group group) {
        GroupOutputFullDto groupDto = groupMapperLite.toGroupOutputFullDto(group);
        if (group.getMembers() == null || group.getMembers().isEmpty()) {
            return groupDto;
        }

        List<UserOutputDto> members = group.getMembers().stream()
                .map(memberMapper::toUserOutputDto)
                .collect(Collectors.toList());
        groupDto.setMembers(members);
        return groupDto;
    }
}
