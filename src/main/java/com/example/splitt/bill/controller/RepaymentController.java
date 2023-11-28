package com.example.splitt.bill.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/groups/{groupId}/repayments/")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class RepaymentController {



}
