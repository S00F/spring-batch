package com.batch.demo.service;

import com.batch.demo.mapper.UserMapper;
import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import com.batch.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
    }


    public User getUserFromUserDto(UserDto userDto) {
        System.out.println("Mapping UserDto to User:");
        System.out.println("Source UserDto: " + userDto);
        User user = userMapper.userDtoToUser(userDto);
        System.out.println("Target User: " + user);
        return user;
    }
}