package com.hcmute.fme.mapper;

import com.hcmute.fme.dto.response.UserDTO;
import com.hcmute.fme.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);
}
