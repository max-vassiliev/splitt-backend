package com.example.splitt.user.service;

import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.mapper.UserMapper;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

//    @Mock
//    private UserRepository userRepository;
//
//    @Spy
//    private UserMapper mapper = Mappers.getMapper(UserMapper.class);
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Test
//    void create_whenValid_returnUserOutDtoWithUserId() {
//        NewUserRequestDto inputDto = createNewUserDto();
//        User savedUser = createUser();
//
//        when(userRepository.save(isA(User.class)))
//                .thenReturn(savedUser);
//
//        UserOutputDto outputDto = userService.create(inputDto);
//
//        assertEquals(savedUser.getId(), outputDto.getId());
//        assertEquals(savedUser.getEmail(), outputDto.getEmail());
//        assertEquals(savedUser.getName(), outputDto.getName());
//
//        verify(userRepository, times(1))
//                .save(isA(User.class));
//    }
//
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
//    private User createUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("Peter");
//        user.setEmail("peter@example.com");
//        user.setPassword("peterpass");
//        return user;
//    }
}