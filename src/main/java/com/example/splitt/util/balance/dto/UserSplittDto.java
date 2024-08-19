package com.example.splitt.util.balance.dto;

import com.example.splitt.bill.dto.validation.IntegerOnly;
import com.example.splitt.bill.dto.validation.StrictIntegerDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSplittDto {

    @NotNull(message = "User ID Not Present. Please add User ID.")
    private Long userId;

    @NotNull(message = "Amount Not Present. Please add amount.")
    @Positive
    @IntegerOnly
    @JsonDeserialize(using = StrictIntegerDeserializer.class)
    private Integer amount;

}
