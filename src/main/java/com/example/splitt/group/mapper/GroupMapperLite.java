package com.example.splitt.group.mapper;

import com.example.splitt.group.dto.input.GroupCreateDto;
import com.example.splitt.group.dto.output.GroupOutputDto;
import com.example.splitt.group.dto.output.GroupOutputFullDto;
import com.example.splitt.group.dto.output.GroupOutputShortDto;
import com.example.splitt.group.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapperLite {

    @Mapping(target = "members", ignore = true)
    Group toGroup(GroupCreateDto groupCreateDto);

    @Mapping(target = "members", ignore = true)
    GroupOutputFullDto toGroupOutputFullDto(Group group);

    GroupOutputShortDto toGroupOutputShortDto(Group group);

    GroupOutputDto toGroupOutputDto(Group group);

}
