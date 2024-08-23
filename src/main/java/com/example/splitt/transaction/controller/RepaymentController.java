package com.example.splitt.transaction.controller;

import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;
import com.example.splitt.transaction.service.RepaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/groups/{groupId}/repayments")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class RepaymentController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final RepaymentService repaymentService;

    @PostMapping
    public RepaymentOutDto add(@PathVariable(name = "groupId") Long groupId,
                               @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                               @RequestParam(name = "pid") Long payerId,
                               @RequestParam(name = "rid") Long recipientId,
                               @Valid @RequestBody RepaymentCreateDto dto) {
        log.info("POST /groups/{}/repayments?pid={}&rid={} | X-Requester-User-Id: {} | Request Body: {}",
                groupId, payerId, recipientId, requesterId, dto);
        dto.setGroupId(groupId);
        dto.setRequesterId(requesterId);
        dto.setPayerId(payerId);
        dto.setRecipientId(recipientId);
        return repaymentService.add(dto);
    }
}
