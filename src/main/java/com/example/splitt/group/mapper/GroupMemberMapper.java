package com.example.splitt.group.mapper;

import com.example.splitt.group.model.GroupMember;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMemberMapper {

    @Mapping(target = "id", source = "groupMember.member.id")
    @Mapping(target = "name", source = "groupMember.member.name")
    @Mapping(target = "email", source = "groupMember.member.email")
    UserOutputDto toUserOutputDto(GroupMember groupMember);

    UserOutputDto toUserOutputDto(User user);

}
