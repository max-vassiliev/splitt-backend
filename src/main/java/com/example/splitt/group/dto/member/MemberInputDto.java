package com.example.splitt.group.dto.member;

import jakarta.validation.constraints.Email;
import lombok.*;

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
