package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillMapperLite {

    String SPLITT_DATE_FORMAT = "yyyy-MM-dd";

    @Mapping(target = "type", expression = "java(BillType.EXPENSE)")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "mapAmount")
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    Bill toExpenseBill(ExpenseCreateDto expenseDto);

    @Named("mapAmount")
    static int mapAmount(Float amount) {
        return (int) (amount * 100);
    }
}
