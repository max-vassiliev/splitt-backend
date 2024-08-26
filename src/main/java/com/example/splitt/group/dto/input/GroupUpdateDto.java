package com.example.splitt.group.dto.input;

import com.example.splitt.util.validation.annotations.AtLeastOneFieldNotNull;
import com.example.splitt.util.validation.annotations.ValidAvatar;
import jakarta.validation.constraints.Size;
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
@AtLeastOneFieldNotNull(
        fields = {"title", "avatar"},
        message = "Either of these fields must be not null: {fields}"
)
public class GroupUpdateDto {

    private Long groupId;

    private Long requesterId;

    @Size(max = 30, message = "Group Title Size Exceeded. The title must not exceed {max} characters.")
    private String title;

    @ValidAvatar
    @Size(max = 50, message = "Group Avatar Name Size Exceeded. Must not exceed {max} characters.")
    private String avatar;

}
