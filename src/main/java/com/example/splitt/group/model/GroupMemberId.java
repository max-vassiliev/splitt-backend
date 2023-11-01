package com.example.splitt.group.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
public class GroupMemberId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;

    public GroupMemberId(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
