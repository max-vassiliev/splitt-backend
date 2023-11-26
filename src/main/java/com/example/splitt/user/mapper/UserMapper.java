package com.example.splitt.user.mapper;

import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutShortDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserRequestDto newUserDto);

    UserOutputDto toUserOutputDto(User user);

    UserOutShortDto toUserOutShortDto(User user);

}
