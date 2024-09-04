package com.example.demo.converter;

import com.example.demo.dao.User;
import com.example.demo.dto.UserDTO;

public class UserConverter {

    public static UserDTO convertUser(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassward(user.getPassword());
        return userDTO;
    }
    public static User convertUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
