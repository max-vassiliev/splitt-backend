package com.example.splitt.group.dto;

import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
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
public class GroupOutputFullDto {

    private Long id;

    private String title;

    private String avatar;

    private List<UserOutputDto> members;

    private List<UserBalanceOutDto> groupBalances;

//    TODO later
//    private List<Bill> bills;
//    private List<Transaction> transactions;

}
