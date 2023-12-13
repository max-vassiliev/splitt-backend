package com.example.splitt.util.balance;

import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplittCalculator {

    public List<UserBalanceOutDto> calculateBalance(List<UserBalance> balances) {
        if (balances.isEmpty()) return Collections.emptyList();


        return null;
    }


}
