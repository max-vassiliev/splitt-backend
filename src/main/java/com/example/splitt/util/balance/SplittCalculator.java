package com.example.splitt.util.balance;

import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.mapper.UserBalanceMapper;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SplittCalculator {

    private final UserBalanceMapper userBalanceMapper;

    public List<UserBalanceOutDto> calculateBalance(List<UserBalance> balances) {
        if (balances.isEmpty()) return Collections.emptyList();
        checkBalancesAreBalanced(balances);

        Map<Long, Map<Long, Integer>> debtors = new HashMap<>();
        Map<Long, Map<Long, Integer>> creditors = new HashMap<>();
        countDebtRec(balances, debtors, creditors);

        return covertForOutput(balances, debtors, creditors);
    }

    // ------------------
    // Auxiliary Methods
    // ------------------

    private void countDebtRec(List<UserBalance> userBalances,
                              Map<Long, Map<Long, Integer>> debtors,
                              Map<Long, Map<Long, Integer>> creditors) {

        UserBalance maxDebtor = userBalances.get(0);
        UserBalance maxCreditor = userBalances.get(0);

        for (UserBalance entry : userBalances) {
            if (entry.getAmount() < maxDebtor.getAmount()) {
                maxDebtor = entry;
            } else if (entry.getAmount() > maxCreditor.getAmount()) {
                maxCreditor = entry;
            }
        }

        if (maxCreditor.getAmount() == 0 && maxDebtor.getAmount() == 0) {
            return;
        }

        int min = Math.min(maxCreditor.getAmount(), -maxDebtor.getAmount());

        updateDebt(min, maxDebtor.getUserId(), maxCreditor.getUserId(), debtors, creditors);

        maxDebtor.setAmount(maxDebtor.getAmount() + min);
        maxCreditor.setAmount(maxCreditor.getAmount() - min);

        countDebtRec(userBalances, debtors, creditors);
    }

    private void updateDebt(int amount, Long debtorId, Long creditorId,
                            Map<Long, Map<Long, Integer>> debtors,
                            Map<Long, Map<Long, Integer>> creditors) {
        if (!debtors.containsKey(debtorId)) {
            debtors.put(debtorId, new HashMap<>());
        }
        if (!creditors.containsKey(creditorId)) {
            creditors.put(creditorId, new HashMap<>());
        }

        Map<Long, Integer> debtorDebts = debtors.get(debtorId);
        Map<Long, Integer> creditorCredits = creditors.get(creditorId);

        debtorDebts.put(creditorId,
                (debtorDebts.getOrDefault(creditorId, 0) - amount));
        creditorCredits.put(debtorId,
                (creditorCredits.getOrDefault(debtorId, 0) + amount));
    }

    private List<UserBalanceOutDto> covertForOutput(List<UserBalance> userBalances,
                                                    Map<Long, Map<Long, Integer>> debtors,
                                                    Map<Long, Map<Long, Integer>> creditors) {

        Map<Long, String> userNames = userBalances.stream()
                .collect(Collectors.toMap(UserBalance::getUserId, UserBalance::getUserName));

        List<UserBalanceOutDto> debtorsDto = debtors.entrySet().stream()
                .map(entry -> userBalanceMapper.toUserBalanceOutDto(entry.getKey(), entry.getValue(), userNames))
                .toList();

        List<UserBalanceOutDto> creditorsDto = creditors.entrySet().stream()
                .map(entry -> userBalanceMapper.toUserBalanceOutDto(entry.getKey(), entry.getValue(), userNames))
                .toList();

        List<UserBalanceOutDto> outputDto = new ArrayList<>();
        outputDto.addAll(debtorsDto);
        outputDto.addAll(creditorsDto);

        return outputDto;
    }

    // -------------
    // Validation
    // -------------

    private void checkBalancesAreBalanced(List<UserBalance> userBalances) {
        if (!areBalanced(userBalances)) {
            throw new CustomValidationException("Group Balances Unbalanced");
        }
    }

    private boolean areBalanced(List<UserBalance> userBalances) {
        int sumOfAmounts = userBalances.stream()
                .mapToInt(UserBalance::getAmount)
                .sum();

        return sumOfAmounts == 0;
    }
}
