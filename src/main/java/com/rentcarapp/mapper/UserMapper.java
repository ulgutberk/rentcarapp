package com.rentcarapp.mapper;

import com.rentcarapp.dto.UserDTO;
import com.rentcarapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "password", ignore = true)
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    UserDTO entityToDto(User user);
}
