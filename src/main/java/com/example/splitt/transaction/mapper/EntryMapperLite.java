package com.example.splitt.transaction.mapper;

import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.util.balance.dto.UserSplittOutDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntryMapperLite {

    UserSplittOutDto toUserSplittOutDto(Entry entry);

}
