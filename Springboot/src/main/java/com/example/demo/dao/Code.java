package com.example.demo.dao;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "code")
public class Code {

    @Id
    @Column(name = "email")
    private String email; // 邮箱地址
    @Column(name = "code")
    private String code;  // 验证码
    @Column(name = "expiretime")
    private LocalDateTime expireTime; // 过期时间


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
