package com.example.demo.controller;

import com.example.demo.dao.Code;
import com.example.demo.services.CodeService;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeController {
    private final CodeService codeService;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public CodeController(CodeService codeService, EmailService emailService, UserService userService) {
        this.codeService = codeService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        try {
            Code code = codeService.generateCode(email);
            if (code != null) {
                emailService.sendVerificationCode(email, code.getCode());
                return ResponseEntity.ok("验证码已发送");
            }
            return ResponseEntity.ok("验证码获取过于频繁，请60秒后再试");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/code/login")
    public ResponseEntity<?> codeaLogin(@RequestParam String email, @RequestParam String code) {
        try {
            if (codeService.validatecode(email, code, false) == 1) {
                return ResponseEntity.ok("登陆成功");
            } else if (codeService.validatecode(email, code, false) == 2) {
                return ResponseEntity.ok("验证码已过期");
            } else if (codeService.validatecode(email, code, false) == 0) {
                return ResponseEntity.ok("请先获取验证码");
            } else if (codeService.validatecode(email, code, false) == -1) {
                return ResponseEntity.ok("请先注册");
            }
            return ResponseEntity.ok("验证码错误");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/code/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password, @RequestParam String code) {
        try {
            if (codeService.validatecode(email, code, true) == 1) {
                userService.addNewUser(email, password);
                return ResponseEntity.ok("注册成功");
            } else if (codeService.validatecode(email, code, true) == 2) {
                return ResponseEntity.ok("验证码已过期");
            } else if (codeService.validatecode(email, code, true) == 0) {
                return ResponseEntity.ok("请先获取验证码");
            } else if (codeService.validatecode(email, code, true) == -1) {
                return ResponseEntity.ok("邮箱已被注册");
            }
            return ResponseEntity.ok("验证码错误");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/code/resetpassword")
    public ResponseEntity<?> resetpassword(@RequestParam String email, @RequestParam String password, @RequestParam String code) {
        try {
            if (codeService.validatecode(email, code, false) == 1) {
                userService.changepassword(email, password);
                return ResponseEntity.ok("修改成功");
            } else if (codeService.validatecode(email, code, false) == 2) {
                return ResponseEntity.ok("验证码已过期");
            } else if (codeService.validatecode(email, code, false) == 0) {
                return ResponseEntity.ok("请先获取验证码");
            } else if (codeService.validatecode(email, code, false) == -1) {
                return ResponseEntity.ok("该邮箱还未注册");
            }
            return ResponseEntity.ok("验证码错误");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
