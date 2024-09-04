package com.example.demo.controller;

import com.example.demo.Response;
import com.example.demo.dao.Course;
import com.example.demo.dto.UserDTO;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/{id}")
    public Response<UserDTO> getUserById(@PathVariable long id){
        return Response.newSuccess(userService.getUserById(id));
    }
    @PostMapping("/user/login")
    public Response<String> login(@RequestBody UserDTO userDTO){
        boolean isValid=userService.login(userDTO);
        if (isValid) {
            // Generate JWT Token or any other response
            return Response.newSuccess("登陆成功");
        } else {
            return Response.newSuccess("邮箱或密码错误");
        }
    }
    @PostMapping("/user/course")
    public ResponseEntity<List<Course>> getCoursesByUserEmail(@RequestParam String email) {
        List<Course> courses = userService.getCoursesByUserEmail(email);
            return ResponseEntity.ok(courses);
    }
}
