package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.expense.ExpenseOutDto;
import com.example.splitt.bill.dto.expense.ExpenseCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentOutDto;
import com.example.splitt.bill.model.bill.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillMapperLite {

    String SPLITT_DATE_FORMAT = "yyyy-MM-dd";

    String SPLITT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String REPAYMENT_DEFAULT_TITLE = "Repayment ##autotitle##";

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    Bill toExpenseBill(ExpenseCreateDto expenseDto);

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    Bill toRepaymentBill(RepaymentCreateDto dto);

    @Mapping(target = "addedBy", ignore = true)
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "addedOn", source = "addedOn", dateFormat = SPLITT_DATE_TIME_FORMAT)
    ExpenseOutDto toExpenseOutDto(Bill bill);

    @Mapping(target = "addedBy", ignore = true)
    @Mapping(target = "title", source = "title", qualifiedByName = "mapRepaymentTitle")
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "addedOn", source = "addedOn", dateFormat = SPLITT_DATE_TIME_FORMAT)
    RepaymentOutDto toRepaymentOutDto(Bill bill);

    // Auxiliary methods

    @Named("mapRepaymentTitle")
    default String mapRepaymentTitle(String title) {
        if (REPAYMENT_DEFAULT_TITLE.equals(title)) {
            return null;
        }
        return title;
    }
}
