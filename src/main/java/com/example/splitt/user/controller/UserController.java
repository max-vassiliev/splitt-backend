package com.example.splitt.user.controller;

import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.dto.UserUpdateDto;
import com.example.splitt.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Slf4j
public class UserController {

    private static final String REQUESTER_ID_HEADER = "X-Requester-User-Id";

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDto create(@Valid @RequestBody NewUserRequestDto newUserDto) {
        log.info("POST /users | newUserDto: {}", newUserDto);
        return userService.create(newUserDto);
    }

    @GetMapping("/user")
    public UserOutputDto getUser(@RequestParam(name= "id", required = false) Long id,
                                 @RequestParam(name = "email", required = false) String email) {
        log.info("GET /users/user?id={}&email={}", id, email);
        return userService.findUser(id, email);
    }

    @PatchMapping("/{userId}")
    public UserOutputDto update(@PathVariable Long userId,
                                @Valid @RequestBody UserUpdateDto updateDto) {
        log.info("PATCH /users/{} | Request Body: {}", userId, updateDto);
        return userService.update(userId, updateDto);
    }
}
