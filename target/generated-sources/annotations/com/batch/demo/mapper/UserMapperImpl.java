package com.batch.demo.mapper;

import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-21T22:41:57+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setUserId( user.getId() );
        userDto.setUserName( user.getName() );
        userDto.setUserEmail( user.getEmail() );

        return userDto;
    }

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getUserId() );
        user.setName( userDto.getUserName() );
        user.setEmail( userDto.getUserEmail() );

        return user;
    }
}
