package com.example.splitt.user;

import com.example.splitt.user.controller.UserController;
import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

//    @Test
//    @SneakyThrows
//    void create_whenValid_thenStatusIsCreatedAndDtoReturned() {
//        NewUserRequestDto inputDto = createNewUserDto();
//        UserOutputDto outputDto = createUserOutDto();
//
//        when(userService.create(isA(NewUserRequestDto.class)))
//                .thenReturn(outputDto);
//
//        mvc.perform(post("/users")
//                    .content(mapper.writeValueAsString(inputDto))
//                    .characterEncoding(StandardCharsets.UTF_8)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(outputDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(outputDto.getName())))
//                .andExpect(jsonPath("$.email", is(outputDto.getEmail())));
//
//        verify(userService, times(1))
//                .create(isA(NewUserRequestDto.class));
//    }
//
//    // ----------
//    // ШАБЛОНЫ
//    // ----------
//
//    private NewUserRequestDto createNewUserDto() {
//        NewUserRequestDto dto = new NewUserRequestDto();
//        dto.setName("Peter");
//        dto.setEmail("peter@example.com");
//        dto.setPassword("peterpass");
//        return dto;
//    }
//
//    private UserOutputDto createUserOutDto() {
//        UserOutputDto dto = new UserOutputDto();
//        dto.setId(1L);
//        dto.setName("Peter");
//        dto.setEmail("peter@example.com");
//        return dto;
//    }
}