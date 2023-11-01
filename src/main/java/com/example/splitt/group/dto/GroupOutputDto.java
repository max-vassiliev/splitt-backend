package com.example.splitt.group.dto;

import com.example.splitt.user.dto.UserOutputDto;
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
public class GroupOutputDto {

    private Long id;

    private String title;

    private List<UserOutputDto> members;

//    TODO later
//    private List<Bill> bills;
//    private List<Transaction> transactions;

}
