package com.example.splitt.page.dto;

import com.example.splitt.group.dto.output.GroupOutputDto;
import com.example.splitt.user.dto.UserOutputDto;
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
public class GroupPageFullDto extends GroupPageDto {

    private Long currentUserId;

    private GroupOutputDto group;

    private List<UserOutputDto> members;

}
