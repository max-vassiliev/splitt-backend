package com.example.splitt.bill.controller;

import com.example.splitt.bill.dto.repayment.RepaymentCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentOutDto;
import com.example.splitt.bill.service.RepaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
