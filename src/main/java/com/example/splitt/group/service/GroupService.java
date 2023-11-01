package com.example.splitt.group.service;

import com.example.splitt.group.dto.*;

import java.util.List;

public interface GroupService {

    GroupOutputDto create(Long requesterId, GroupCreateDto dto);

    GroupOutputDto findById(Long groupId, Long requesterId);

    List<GroupOutputShortDto> findAllByUserId(Long userId);

    GroupOutputShortDto updateProperties(GroupUpdateDto dto);

    GroupOutputDto updateMembers(GroupUpdateMembersDto dto);
}
