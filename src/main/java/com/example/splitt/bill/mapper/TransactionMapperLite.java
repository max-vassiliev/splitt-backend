package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.shares.UserSplitOutDto;
import com.example.splitt.bill.model.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapperLite {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "amountToInt")
    @Mapping(target = "userName", source = "user.name")
    UserSplitOutDto toUserSplitOutDto(Transaction transaction);

    @Named("amountToInt")
    static float mapAmountToFloat(int amount) {
        return (float) amount / 100;
    }
}
