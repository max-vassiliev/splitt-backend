package com.example.splitt.transaction.model.transaction;

import com.example.splitt.transaction.model.entry.Entry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transactions", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "splitt_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SplittType splittType;

    @Column(name = "added_by", nullable = false)
    private Long addedByUserId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "emoji")
    private String emoji;

    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "added_on", nullable = false)
    private LocalDateTime addedOn;

    @Transient
    private List<Entry> payments;

    @Transient
    private List<Entry> splitts;

    @Transient
    private Entry repaymentTo;

    @Transient
    private Entry repaymentFrom;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return Objects.equals(id, transaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
