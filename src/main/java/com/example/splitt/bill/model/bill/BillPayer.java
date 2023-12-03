package com.example.splitt.bill.model.bill;

import com.example.splitt.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "bills_paid_by", schema = "public")
@IdClass(BillPayerId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillPayer {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private User payer;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPayer that = (BillPayer) o;
        return Objects.equals(bill.getId(), that.bill.getId()) && Objects.equals(payer, that.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bill, payer);
    }
}
