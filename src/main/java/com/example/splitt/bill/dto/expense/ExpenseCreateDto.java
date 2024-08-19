package com.example.splitt.bill.dto.expense;

import com.example.splitt.bill.dto.validation.IntegerOnly;
import com.example.splitt.bill.dto.validation.StrictIntegerDeserializer;
import com.example.splitt.util.balance.dto.UserSplittDto;
import com.example.splitt.error.exception.CustomValidationException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseCreateDto {

    private Long requesterId;

    private Long groupId;

    @NotBlank(message = "Title Absent. Please add title.")
    @Size(max = 50, message = "Title Size Exceeded. The title most not exceed 50 characters.")
    private String title;

    @Size(max = 250, message = "Note Size Exceeded. The note must be less than 250 characters long.")
    private String note;

    @NotNull(message = "Expense Amount Absent. Please add expense amount.")
    @Positive
    @IntegerOnly
    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    private Integer amount;

    @NotBlank(message = "Expense Date Missing. Please add the date the expense was made.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Date Format Error. The date must be written in the following format: 'yyyy-MM-dd'.")
    private String date;

    @Valid
    List<UserSplittDto> paidBy;

    @Valid
    List<UserSplittDto> debtShares;

    public void validatePaidByNotEmpty() {
        if (isEmpty(paidBy)) {
            throw new CustomValidationException("Paid By List Is Empty. " +
                    "Missing data on who paid for the expense.");
        }
    }

    public void validateDebtSharesNotEmpty() {
        if (isEmpty(debtShares)) {
            throw new CustomValidationException("Debt Shares List Is Empty. " +
                    "Missing data users' debts.");
        }
    }

    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}
