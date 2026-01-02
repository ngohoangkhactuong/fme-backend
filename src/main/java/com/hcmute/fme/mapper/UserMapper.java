package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name().toLowerCase())")
    @Mapping(target = "isActive", source = "isActive")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);
}
