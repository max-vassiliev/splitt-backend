package com.example.splitt.bill.mapper;

import com.example.splitt.util.balance.dto.UserSplittOutDto;
import com.example.splitt.bill.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapperLite {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    UserSplittOutDto toUserSplitOutDto(Transaction transaction);

}
