package com.example.splitt.group.mapper;

import com.example.splitt.group.dto.GroupOutputDto;
import com.example.splitt.group.dto.GroupOutputShortDto;
import com.example.splitt.group.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapperLite {

    @Mapping(target = "members", ignore = true)
    GroupOutputDto toGroupOutputDto(Group group);

    GroupOutputShortDto toGroupOutputShortDto(Group group);

}
