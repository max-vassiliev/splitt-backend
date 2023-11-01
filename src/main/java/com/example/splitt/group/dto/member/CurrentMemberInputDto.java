package com.example.splitt.group.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CurrentMemberInputDto extends MemberInputDto {

    @NotBlank(message = "Blank ID. Please add the user's ID.")
    private Long id;

}
