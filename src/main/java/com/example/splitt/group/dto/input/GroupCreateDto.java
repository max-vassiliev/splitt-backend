package com.example.splitt.group.dto.input;

import com.example.splitt.group.dto.member.NewMemberInputDto;
import com.example.splitt.util.validation.annotations.ValidAvatar;
import jakarta.validation.constraints.NotBlank;
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
public class GroupCreateDto {

    @NotBlank(message = "Group Title Is Empty. Please add group title.")
    @Size(max = 30, message = "Group Title Size Exceeded. The title must not exceed {max} characters.")
    private String title;

    @ValidAvatar
    @Size(max = 50, message = "Group Avatar Name Size Exceeded. Must not exceed {max} characters.")
    private String avatar;

    private List<NewMemberInputDto> members;

}
