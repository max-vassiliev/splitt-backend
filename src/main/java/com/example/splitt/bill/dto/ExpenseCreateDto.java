package com.example.splitt.bill.dto;

import com.example.splitt.error.exception.CustomValidationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ExpenseCreateDto {

    private Long requesterId;

    private Long groupId;

    @NotBlank(message = "Expense Title Absent. Please add title.")
    private String title;

    @NotBlank(message = "Expense Amount Absent. Please add expense amount.")
    private Float amount;

    @Valid
    List<UserSplitDto> paidBy;

    @Valid
    List<UserSplitDto> debtShares;

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
