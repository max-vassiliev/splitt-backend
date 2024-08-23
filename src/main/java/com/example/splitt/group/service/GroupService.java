package com.example.splitt.group.service;

import com.example.splitt.group.dto.GroupCreateDto;
import com.example.splitt.group.dto.GroupOutputFullDto;
import com.example.splitt.group.dto.GroupOutputShortDto;
import com.example.splitt.group.dto.GroupUpdateDto;
import com.example.splitt.group.dto.GroupUpdateMembersDto;
import com.example.splitt.group.dto.page.GroupPageFullDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {

    GroupOutputFullDto create(Long requesterId, GroupCreateDto dto);

    GroupOutputFullDto findById(Long groupId, Long requesterId);

    List<GroupOutputShortDto> findAllByUserId(Long userId);

    GroupOutputShortDto updateProperties(GroupUpdateDto dto);

    GroupOutputFullDto updateMembers(GroupUpdateMembersDto dto);

    GroupPageFullDto getGroupFullPageById(Long groupId, Long requesterId, Pageable pageable);

}
