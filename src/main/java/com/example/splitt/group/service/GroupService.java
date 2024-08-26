package com.example.splitt.group.service;

import com.example.splitt.group.dto.input.GroupCreateDto;
import com.example.splitt.group.dto.output.GroupOutputDto;
import com.example.splitt.group.dto.output.GroupOutputFullDto;
import com.example.splitt.group.dto.output.GroupOutputShortDto;
import com.example.splitt.group.dto.input.GroupUpdateDto;
import com.example.splitt.group.dto.member.GroupUpdateMembersDto;

import java.util.List;

public interface GroupService {

    GroupOutputFullDto create(Long requesterId, GroupCreateDto dto);

    GroupOutputFullDto findById(Long groupId, Long requesterId);

    List<GroupOutputShortDto> findAllByUserId(Long userId);

    GroupOutputDto updateProperties(GroupUpdateDto dto);

    GroupOutputFullDto updateMembers(GroupUpdateMembersDto dto);

}
