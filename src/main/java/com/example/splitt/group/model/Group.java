package com.example.splitt.group.model;

import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups", schema = "public")
@Getter
@Setter
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "avatar")
    private String avatar;

    @Transient
    private Set<User> members;

    @Transient
    private List<Transaction> transactions;

    @Transient
    private List<Entry> entries;


    public Group() {
        this.members = new HashSet<>();
        this.transactions = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

    public Group(String title) {
        this.title = title;
        this.members = new HashSet<>();
        this.transactions = new ArrayList<>();
        this.entries = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(title, group.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
