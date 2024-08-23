package com.example.splitt.group.mapper;

import com.example.splitt.group.dto.GroupOutputDto;
import com.example.splitt.group.dto.GroupOutputFullDto;
import com.example.splitt.group.dto.GroupOutputShortDto;
import com.example.splitt.group.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapperLite {

    @Mapping(target = "members", ignore = true)
    GroupOutputFullDto toGroupOutputFullDto(Group group);

    GroupOutputShortDto toGroupOutputShortDto(Group group);

    GroupOutputDto toGroupOutputDto(Group group);

}
