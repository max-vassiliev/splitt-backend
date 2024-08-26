package com.example.splitt.group.controller;

import com.example.splitt.group.dto.input.GroupCreateDto;
import com.example.splitt.group.dto.output.GroupOutputDto;
import com.example.splitt.group.dto.output.GroupOutputFullDto;
import com.example.splitt.group.dto.output.GroupOutputShortDto;
import com.example.splitt.group.dto.input.GroupUpdateDto;
import com.example.splitt.group.dto.member.GroupUpdateMembersDto;
import com.example.splitt.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/groups")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class GroupController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public GroupOutputFullDto getById(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                      @PathVariable(name = "groupId") Long groupId) {
        log.info("GET /groups/{} | X-Requester-User-Id: {} ", groupId, requesterId);
        return groupService.findById(groupId, requesterId);
    }

    @GetMapping
    public List<GroupOutputShortDto> getAllByUserId(@RequestHeader(REQUESTER_ID_HEADER) Long userId) {
        log.info("GET /groups | X-Requester-User-Id: {} ", userId);
        return groupService.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupOutputFullDto create(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                     @Valid @RequestBody GroupCreateDto dto) {
        log.info("POST /groups | X-Requester-User-Id: {} | Request Body: {}", requesterId, dto);
        return groupService.create(requesterId, dto);
    }

    @PatchMapping("/{groupId}")
    public GroupOutputDto updateProperties(@PathVariable Long groupId,
                                           @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                           @Valid @RequestBody GroupUpdateDto dto) {
        log.info("PATCH /groups/{} | X-Requester-User-Id: {} | Request Body: {}",
                groupId, requesterId, dto);
        dto.setGroupId(groupId);
        dto.setRequesterId(requesterId);
        return groupService.updateProperties(dto);
    }

    @PatchMapping("/{groupId}/members")
    public GroupOutputFullDto updateMembers(@PathVariable Long groupId,
                                            @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                            @Valid @RequestBody GroupUpdateMembersDto dto) {
        log.info("PATCH /groups/{}/members | X-Requester-User-Id: {} | Request Body: {}",
                groupId, requesterId, dto);
        dto.setGroupId(groupId);
        dto.setRequesterId(requesterId);
        return groupService.updateMembers(dto);
    }
}
