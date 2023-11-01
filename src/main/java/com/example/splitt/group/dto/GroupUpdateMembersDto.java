package com.example.splitt.group.dto;

import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.group.dto.member.CurrentMemberInputDto;
import com.example.splitt.group.dto.member.NewMemberInputDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupUpdateMembersDto {

    private Long groupId;

    private Long requesterId;

    private List<CurrentMemberInputDto> currentMembers;

//    private List<MemberInputDto> newMembers;
    private List<NewMemberInputDto> newMembers;

    public void validateMembersListsNotEmpty() {
        if ((currentMembers == null || currentMembers.isEmpty()) && (newMembers == null || newMembers.isEmpty())) {
            throw new CustomValidationException("No Data To Update. Current and new members lists are empty.");
        }
    }
}
