package com.example.splitt.group.dto;

import com.example.splitt.group.dto.member.MemberInputDto;
import com.example.splitt.group.dto.member.CurrentMemberInputDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupUpdateDto {

    private Long groupId;

    private Long requesterId;

    private String title;

    private List<CurrentMemberInputDto> currentMembers;

    private List<MemberInputDto> newMembers;

}
