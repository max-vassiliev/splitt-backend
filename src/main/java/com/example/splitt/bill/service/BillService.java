package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.util.model.CustomPageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {
    List<BillOutDto> findAllByGroupId(Long groupId, Long requesterId, Pageable pageable);
}
