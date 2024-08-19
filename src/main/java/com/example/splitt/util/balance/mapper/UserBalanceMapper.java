package com.example.splitt.util.balance.mapper;

import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplittOutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserBalanceMapper {

    public UserBalanceOutDto toUserBalanceOutDto(Long userId,
                                                 Map<Long, Integer> shares,
                                                 Map<Long, String> userNames) {
        int balance = shares.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        List<UserSplittOutDto> details = shares.entrySet().stream()
                .map(entry -> toUserSplitOutDto(entry.getKey(),
                        userNames.get(entry.getKey()),
                        entry.getValue()))
                .toList();

        UserBalanceOutDto dto = new UserBalanceOutDto();
        dto.setUserId(userId);
        dto.setUserName(userNames.get(userId));
        dto.setBalance(balance);
        dto.setDetails(details);
        return dto;
    }

    private UserSplittOutDto toUserSplitOutDto(Long userId, String userName, int amount) {
        UserSplittOutDto dto = new UserSplittOutDto();
        dto.setUserId(userId);
        dto.setUserName(userName);
        dto.setAmount(amount);
        return dto;
    }

}
