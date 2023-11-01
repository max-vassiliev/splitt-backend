package com.example.splitt.group.dto.member;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberInputDto {

    private String name;

    @Email(message = "Email Not Well-Formed. Must be a well-formed email address.")
    private String email;

}
