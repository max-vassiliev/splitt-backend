package com.example.splitt.page.service;

import com.example.splitt.page.dto.GroupPageDto;
import com.example.splitt.page.dto.GroupPageFullDto;
import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupPageService {

    GroupPageFullDto getByGroupIdFull(Long groupId, Long requesterId, Pageable pageable);

    GroupPageDto getByGroupId(Long groupId, Long requesterId, Pageable pageable);

    List<TransactionOutShortDto> getByGroupIdTransactions(Long groupId,
                                                          Long requesterId,
                                                          Pageable pageable);
}
