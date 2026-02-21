package com.batch.demo.batch;

import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import com.batch.demo.service.UserService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserItemProcessor implements ItemProcessor<UserDto,User> {

    private final UserService userService;

    public UserItemProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User process(UserDto user) throws Exception {
        // Use the UserService to map the UserDto  to an entity
        System.out.println("Processing User: " + user.getUserName() + " -> Mapping to Entity");
        return userService.getUserFromUserDto(user);
    }

}