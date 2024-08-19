package com.example.splitt.bill.dto;

import com.example.splitt.util.balance.dto.UserSplittOutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupBalanceOutDto {

    private Long userId;

    private String userName;

    private Integer balance;

    private List<UserSplittOutDto> details;

}
