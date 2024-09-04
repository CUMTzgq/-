package com.example.demo.services;

import com.example.demo.dao.Course;
import com.example.demo.dao.User;
import com.example.demo.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(long id);


    Long addNewUser(String email, String password);

    boolean login(UserDTO userDTO);

    void changepassword(String email, String password);

    List<Course> getCoursesByUserEmail(String email);
}
