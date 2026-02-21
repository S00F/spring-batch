package com.batch.demo.mapper;

import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "name", target = "userName")
    @Mapping(source = "email", target = "userEmail")
    UserDto userToUserDto(User user);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userName", target = "name")
    @Mapping(source = "userEmail", target = "email")
    User userDtoToUser(UserDto userDto);
}