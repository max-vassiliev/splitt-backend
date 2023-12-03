package com.example.splitt.group.repository;

import com.example.splitt.group.model.GroupMember;
import com.example.splitt.group.model.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    List<GroupMember> findByIdGroupId(Long groupId);

    List<GroupMember> findByIdUserId(Long userId);

    List<GroupMember> findByIdIn(List<GroupMemberId> ids);

}
