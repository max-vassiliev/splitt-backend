package com.example.splitt.user.service;

import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.dto.UserUpdateDto;

public interface UserService {

    UserOutputDto create(NewUserRequestDto newUserDto);

    UserOutputDto findUser(Long id, String email);

    UserOutputDto update(Long userId, UserUpdateDto updateDto);

}
