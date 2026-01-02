package com.hcmute.fme.service;

import com.hcmute.fme.dto.response.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll();

    UserDTO getById(Long id);

    UserDTO getByEmail(String email);

    void deactivate(Long id);

    void activate(Long id);
}
