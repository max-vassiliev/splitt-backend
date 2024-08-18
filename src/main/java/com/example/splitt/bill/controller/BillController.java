package com.example.splitt.bill.controller;

import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.bill.service.BillService;
import com.example.splitt.util.model.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/groups/{groupId}/bills")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class BillController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final BillService billService;

    @GetMapping
    public List<BillOutDto> findAll(@PathVariable(name = "groupId") Long groupId,
                                    @RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                    @RequestParam(name = "size", defaultValue = "15") int size) {
        log.info("GET /groups/{}/bills?from={}&size={} | X-Requester-User-Id: {}",
                groupId, from, size, requesterId);
        return billService.findAllByGroupId(groupId, requesterId,
                new CustomPageRequest(from, size, Sort.by(Sort.Direction.DESC, "b.date")));
    }
}
