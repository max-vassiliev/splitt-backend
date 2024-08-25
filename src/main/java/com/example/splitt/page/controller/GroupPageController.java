package com.example.splitt.page.controller;

import com.example.splitt.page.dto.GroupPageDto;
import com.example.splitt.page.service.GroupPageService;
import com.example.splitt.page.dto.GroupPageFullDto;
import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
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
@RequestMapping(path = "/groups/{groupId}/page")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class GroupPageController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final GroupPageService groupPageService;


    @GetMapping("/full")
    public GroupPageFullDto getByGroupIdFull(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                                 @PathVariable(name = "groupId") Long groupId,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET /groups/{}/page/full?from={}&size={} | X-Requester-User-Id: {} ",
                groupId, from, size, requesterId);
        return groupPageService.getByGroupIdFull(groupId, requesterId,
                new CustomPageRequest(from, size, Sort.by(Sort.Direction.DESC, "t.date")));
    }

    @GetMapping
    public GroupPageDto getByGroupId(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                     @PathVariable(name = "groupId") Long groupId,
                                     @RequestParam(name = "from", defaultValue = "0") int from,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("GET /groups/{}/page?from={}&size={} | X-Requester-User-Id: {} ",
                groupId, from, size, requesterId);
        return groupPageService.getByGroupId(groupId, requesterId,
                new CustomPageRequest(from, size, Sort.by(Sort.Direction.DESC, "t.date")));
    }


    @GetMapping("/transactions")
    public List<TransactionOutShortDto> getByGroupIdTransactions(@RequestHeader(REQUESTER_ID_HEADER) Long requesterId,
                                                                 @PathVariable(name = "groupId") Long groupId,
                                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size)  {
        log.info("GET /groups/{}/page/transactions?from={}&size={} | X-Requester-User-Id: {} ",
                groupId, from, size, requesterId);
        return groupPageService.getByGroupIdTransactions(groupId, requesterId,
                new CustomPageRequest(from, size, Sort.by(Sort.Direction.DESC, "t.date")));
    }
}
