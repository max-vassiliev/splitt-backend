package com.example.splitt.bill.controller;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.service.ExpenseService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/groups/{groupId}/expenses/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class ExpenseController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseBalanceOutDto add(@PathVariable(name = "groupId") Long groupId,
                                    @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                    @Valid @RequestBody ExpenseCreateDto expenseDto) {
        log.info("POST /groups/{}/expenses/ | X-Requester-User-Id: {} | Request Body: {}", groupId,
                requesterId, expenseDto);
        expenseDto.setRequesterId(requesterId);
        expenseDto.setGroupId(groupId);
        return expenseService.add(expenseDto);
    }
}
