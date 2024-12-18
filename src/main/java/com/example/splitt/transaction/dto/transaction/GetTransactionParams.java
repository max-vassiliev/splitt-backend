package com.example.splitt.transaction.dto.transaction;

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
public class GetTransactionParams {

    private Long transactionId;

    private Long groupId;

    private Long requesterId;

}
