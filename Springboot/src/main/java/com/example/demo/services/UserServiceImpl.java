package com.example.demo.services;

import com.example.demo.converter.UserConverter;
import com.example.demo.dao.*;
import com.example.demo.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Override
    public UserDTO getUserById(long id) {
        User user= userRepository.findById(id).orElseThrow(RuntimeException::new);
        return UserConverter.convertUser(user);
    }

    @Override
    public Long addNewUser(String email,String password) {
        User user=new User();
        user.setEmail(email);
        user.setPassword(password);
        user=userRepository.save(user);
        return user.getId();
    }
    public void changepassword(String email,String password){
        User user = userRepository.findByEmail(email).orElse(null);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public boolean login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        return user != null && user.getPassword().equals(userDTO.getPassword());
    }
    public List<Course> getCoursesByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
            List<UserCourse> userCourses = userCourseRepository.findByUid(user.getId());
            List<Course> courses = new ArrayList<>();
            for (UserCourse userCourse : userCourses) {
                courseRepository.findById(userCourse.getCid()).ifPresent(courses::add);
            }
            return courses;
    }


}
