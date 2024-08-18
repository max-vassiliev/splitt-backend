package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.bill.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

//    @Query("select b from Bill b " +
//            "left join Transaction t on t.bill.id = b.id " +
//            "where t.group.id = :groupId")
    @Query("select distinct b from Bill b " +
        "join Transaction t on b.id = t.bill.id " +
        "where t.group.id = :groupId")
    List<Bill> findAllByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}
