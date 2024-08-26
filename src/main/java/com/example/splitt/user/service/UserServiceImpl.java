package com.example.splitt.user.service;

import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.user.dto.NewUserRequestDto;
import com.example.splitt.user.dto.UserOutputDto;
import com.example.splitt.user.dto.UserUpdateDto;
import com.example.splitt.user.mapper.UserMapper;
import com.example.splitt.user.model.User;
import com.example.splitt.user.repository.UserRepository;
import com.example.splitt.util.AppConstants;
import com.example.splitt.util.validation.SplittValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final SplittValidator splittValidator;


    @Override
    @Transactional
    public UserOutputDto create(NewUserRequestDto newUserDto) {
        validateOnCreate(newUserDto);
        User user = userMapper.toUser(newUserDto);
        return userMapper.toUserOutputDto(userRepository.save(user));
    }

    @Override
    public UserOutputDto findUser(Long id, String email) {
        validateOnFindUser(id, email);
        User user = findUserByParams(id, email);
        return userMapper.toUserOutputDto(user);
    }

    @Override
    @Transactional
    public UserOutputDto update(Long userId, UserUpdateDto updateDto) {
        User user = getById(userId);
        validateOnUpdate(updateDto, user);
        updateFields(updateDto, user);
        return userMapper.toUserOutputDto(user);
    }

    // ------------------
    // Repository
    // ------------------

    private User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found. Requested ID: " + id,
                        User.class
                ));
    }

    private User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found. Requested email: " + email,
                        User.class
                ));
    }

    private User getByIdAndEmail(Long id, String email) {
        return userRepository.findByIdAndEmail(id, email)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User Not Found. Requested ID: %d. Requested Email: %s", id, email),
                        User.class
                ));
    }

    private void updateFields(UserUpdateDto updateDto, User user) {
        if (updateDto.getName() != null) {
            user.setName(updateDto.getName());
        }
        if (updateDto.getEmail() != null) {
            user.setEmail(updateDto.getEmail());
        }
        if (updateDto.getAvatar() != null) {
            String updatedAvatar = updateDto.getAvatar().equals(AppConstants.DEFAULT_AVATAR)
                    ? null
                    : updateDto.getAvatar();
            user.setAvatar(updatedAvatar);
        }
        if (updateDto.getPassword() != null) {
            user.setPassword(updateDto.getPassword());
        }
        userRepository.flush();
    }

    // ------------------
    // Private methods
    // ------------------

    private User findUserByParams(Long id, String email) {
        if (id != null && email != null) {
            return getByIdAndEmail(id, email);
        }
        if (email != null) {
            return getByEmail(email);
        }
        return getById(id);
    }

    // ------------------
    // Validation
    // ------------------

    private void validateOnCreate(NewUserRequestDto newUserDto) {
        validateEmailNotEmpty(newUserDto.getEmail());
        validatePasswordNotEmpty(newUserDto.getPassword());
        validateEmailWithPassword(newUserDto.getEmail(), newUserDto.getPassword(), null);
    }

    private void validateOnUpdate(UserUpdateDto updateDto, User user) {
        validateNameNotEmpty(updateDto.getName());
        validateEmailNotEmpty(updateDto.getEmail());
        validatePasswordNotEmpty(updateDto.getPassword());
        validateEmailWithPassword(updateDto.getEmail(), updateDto.getPassword(), user);
    }

    private void validateOnFindUser(Long id, String email) {
        if (id == null && (email == null || email.trim().isEmpty())) {
            throw new CustomValidationException("ID or Email Required. " +
                    "Cannot find users without an ID or email.");
        }
    }

    private void validateNameNotEmpty(String name) {
        if (splittValidator.isEmpty(name)) {
            throw new CustomValidationException("Name Empty. Insert name.");
        }
    }

    private void validatePasswordNotEmpty(String password) {
        if (splittValidator.isEmpty(password)) {
            throw new CustomValidationException("Password Empty. " +
                    "Please write a password of at least 8 characters.");
        }
    }

    private void validateEmailNotEmpty(String email) {
        if (splittValidator.isEmpty(email)) {
            throw new CustomValidationException("Email Empty. " +
                    "Must be a well-formed email address.");
        }
    }

    private void validateEmailWithPassword(String email, String password, User user) {
        boolean isPasswordWithNoEmail = user != null ?
                password != null && (email == null && user.getEmail() == null) :
                password != null && email == null;

        if (isPasswordWithNoEmail) {
            throw new CustomValidationException("Email With Password Required. " +
                    "Please add an email address.");
        }
    }
}
