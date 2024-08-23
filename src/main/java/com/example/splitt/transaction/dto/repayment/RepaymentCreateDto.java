package com.example.splitt.transaction.dto.repayment;

import com.example.splitt.transaction.dto.validation.IntegerOnly;
import com.example.splitt.transaction.dto.validation.StrictIntegerDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Amount Missing. Please add the repayment amount.")
    @Positive
    @IntegerOnly
    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    private Integer amount;

    @Size(max = 2, message = "Emoji Size Exceeded. The field should contain a maximum of 2 characters.")
    private String emoji;

    @NotBlank(message = "Date Missing. Please add the date the repayment was made.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",
            message = "Date Format Error. The date must be written in the following format: 'yyyy-MM-dd'.")
    private String date;

    @Size(max = 250, message = "Note Size Exceeded. The note must be less than 250 characters long.")
    private String note;

    public boolean isPayerRecipient() {
        return Objects.equals(this.payerId, this.recipientId);
    }

}
