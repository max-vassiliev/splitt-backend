package com.example.splitt.bill.dto.repayment;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepaymentCreateDto {

    private Long requesterId;

    private Long groupId;

    private Long payerId;

    private Long recipientId;

    @Size(max = 50, message = "Title Size Exceeded. The title most not exceed 50 characters.")
    private String title;

    @Size(max = 250, message = "Note Size Exceeded. The note must be less than 250 characters long.")
    private String note;

    @NotNull(message = "Amount Missing. Please add the repayment amount.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Amount Value Error. The amount must be greater than 0.")
    private Float amount;

    @NotBlank(message = "Date Missing. Please add the date the repayment was made.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Date Format Error. The date must be written in the following format: 'yyyy-MM-dd'.")
    private String date;

    public boolean isRequesterPayer() {
        return Objects.equals(this.requesterId, this.payerId);
    }

    public boolean isRequesterRecipient() {
        return Objects.equals(this.requesterId, this.recipientId);
    }

    public boolean isPayerRecipient() {
        return Objects.equals(this.payerId, this.recipientId);
    }
}
