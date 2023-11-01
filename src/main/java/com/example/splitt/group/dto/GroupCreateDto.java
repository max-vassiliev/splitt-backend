package com.example.splitt.group.dto;

import com.example.splitt.group.dto.member.NewMemberInputDto;
import jakarta.validation.constraints.NotBlank;
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
public class GroupCreateDto {

    @NotBlank(message = "Group Title Is Empty. Please add group title.")
    private String title;

    private List<NewMemberInputDto> members;

}
