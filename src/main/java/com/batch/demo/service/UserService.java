package com.batch.demo.service;

import com.batch.demo.mapper.UserMapper;
import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserFromUserDto(UserDto userDto) {
        logger.debug("Mapping UserDto to User: source={}", userDto);
        User user = userMapper.userDtoToUser(userDto);
        logger.debug("Mapped result: {}", user);
        return user;
    }
}