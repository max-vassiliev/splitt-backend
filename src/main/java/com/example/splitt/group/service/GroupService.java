package com.example.splitt.group.service;

import com.example.splitt.group.dto.GroupCreateDto;
import com.example.splitt.group.dto.GroupOutputDto;
import com.example.splitt.group.dto.GroupOutputShortDto;
import com.example.splitt.group.dto.GroupUpdateDto;
import com.example.splitt.group.dto.GroupUpdateMembersDto;

import java.util.List;

public interface GroupService {

    GroupOutputDto create(Long requesterId, GroupCreateDto dto);

    GroupOutputDto findById(Long groupId, Long requesterId);

    List<GroupOutputShortDto> findAllByUserId(Long userId);

    GroupOutputShortDto updateProperties(GroupUpdateDto dto);

    GroupOutputDto updateMembers(GroupUpdateMembersDto dto);

}
