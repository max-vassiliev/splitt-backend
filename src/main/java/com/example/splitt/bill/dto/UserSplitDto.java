package com.example.splitt.bill.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "User ID Not Present. Please add User Id.")
    private Long userId;

    @NotBlank(message = "Amount Not Present. Please add amount.")
    private Float splitAmount;

}
