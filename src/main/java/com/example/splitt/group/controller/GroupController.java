package com.example.splitt.group.controller;

import com.example.splitt.group.dto.*;
import com.example.splitt.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public GroupOutputDto getById(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
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
    public GroupOutputDto create(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                 @Valid @RequestBody GroupCreateDto dto) {
        log.info("POST /groups | X-Requester-User-Id: {} | Request Body: {}", requesterId, dto);
        return groupService.create(requesterId, dto);
    }

    @PatchMapping("/{groupId}")
    public GroupOutputShortDto updateProperties(@PathVariable Long groupId,
                                 @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                 @Valid @RequestBody GroupUpdateDto dto) {
        log.info("PATCH /groups/{} | X-Requester-User-Id: {} | Request Body: {}",
                groupId, requesterId, dto);
        dto.setGroupId(groupId);
        dto.setRequesterId(requesterId);
        return groupService.updateProperties(dto);
    }

    @PatchMapping("/{groupId}/members")
    public GroupOutputDto updateMembers(@PathVariable Long groupId,
                                        @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                        @Valid @RequestBody GroupUpdateMembersDto dto) {
        log.info("PATCH /groups/{}/members | X-Requester-User-Id: {} | Request Body: {}",
                groupId, requesterId, dto);
        dto.setGroupId(groupId);
        dto.setRequesterId(requesterId);
        return groupService.updateMembers(dto);
    }
}
