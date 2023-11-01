package com.example.splitt.group.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewMemberInputDto extends MemberInputDto {

    @Override
    @NotBlank(groups = NewMemberValidationGroup.class, message = "Name Empty. Insert name.")
    public String getName() {
        return super.getName();
    }
}
