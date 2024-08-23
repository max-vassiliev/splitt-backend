package com.example.splitt.group.dto;

import com.example.splitt.group.dto.member.MemberInputDto;
import com.example.splitt.group.dto.member.CurrentMemberInputDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupUpdateDto {

    private Long groupId;

    private Long requesterId;

    @Size(max = 30, message = "Group Title Size Exceeded. The title must not exceed {max} characters.")
    private String title;

    @Size(max = 50, message = "Group Avatar Name Size Exceeded. Must not exceed {max} characters.")
    private String avatar;

    private List<CurrentMemberInputDto> currentMembers;

    private List<MemberInputDto> newMembers;

}
