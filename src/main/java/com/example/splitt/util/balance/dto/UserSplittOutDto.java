package com.example.splitt.util.balance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserSplittOutDto {

    private Long userId;

    private Integer amount;

}
