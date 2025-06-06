package com.example.splitt.transaction.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPayerId implements Serializable {

    private Long transaction;

    private Long payer;

}
