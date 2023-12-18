package com.example.splitt.util.balance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserSplitOutDto extends UserSplitDto {

    private String userName;

    public UserSplitOutDto(Long userId, String userName, Float amount) {
        super(userId, amount);
        this.userName = userName;
    }
}
