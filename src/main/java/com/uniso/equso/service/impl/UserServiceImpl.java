package com.uniso.equso.service.impl;

import com.uniso.equso.dao.entities.UserEntity;
import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import com.uniso.equso.dao.repository.UserEntityRepository;
import com.uniso.equso.model.CreateUserRequest;
import com.uniso.equso.model.UserDto;
import com.uniso.equso.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userEntityRepository;

    public UserServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public void addUser(CreateUserRequest userRequest) {
        var userEntity = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .alias(UUID.randomUUID().toString())
                .isAnonymous(false)
                .password(userRequest.getPassword())
                .type(UserType.USER)
                .subType(UserSubType.DEFAULT)
                .status(Status.ACTIVE)
                .build();

        userEntityRepository.save(userEntity);
    }

    @Override
    public UserDto getUserById(Long userId) {
        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new RuntimeException("exception.user-not-found");
                });

        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .type(user.getType())
                .subType(user.getSubType())
                .build();
    }
}
