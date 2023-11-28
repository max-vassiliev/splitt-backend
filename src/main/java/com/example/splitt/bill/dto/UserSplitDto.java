package com.example.splitt.bill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserSplitDto {

    @NotNull(message = "User ID Not Present. Please add User ID.")
    private Long userId;

    @NotNull(message = "Amount Not Present. Please add amount.")
    private Float amount;

}
