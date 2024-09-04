package com.example.demo.services;

import com.example.demo.dao.Code;
import com.example.demo.dao.CodeRepository;
import com.example.demo.dao.User;
import com.example.demo.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class CodeService {
    @Autowired
    private   CodeRepository codeRepository;
    @Autowired
    private UserRepository userRepository;
    private final Random random = new Random();

    public  int validatecode(String email, String code,boolean isregiteration) {
        Optional<User> existingUserOption =userRepository.findByEmail(email);
        if(!existingUserOption.isPresent()&&!isregiteration){
            return -1;
        }
        if(existingUserOption.isPresent()&&isregiteration){
            return -1;
        }
        Optional<Code> existingCodeOptional = codeRepository.findFirstByEmail(email);

        if(!existingCodeOptional.isPresent()){
            return 0;
        }
        Code newcode=existingCodeOptional.get();
        if(code.equals(newcode.getCode())&&newcode.getExpireTime().isAfter(LocalDateTime.now())){
            return 1;
        } else if (code.equals(newcode.getCode())&&LocalDateTime.now().isAfter(newcode.getExpireTime())) {
            return 2;
        }
        return 3;
    }

    public Code generateCode(String email) {
        // 生成随机验证码
        String code = String.valueOf(random.nextInt(899999) + 100000);
        Optional<Code> existingCodeOptional = codeRepository.findFirstByEmail(email);
        Code newCode;
        // 创建新的验证码记录
        if (existingCodeOptional.isPresent()) {
            // 存在现有记录，更新现有记录
            Code existingCode = existingCodeOptional.get();
            if(LocalDateTime.now().isAfter(existingCode.getExpireTime().minusMinutes(4))){
                existingCode.setCode(code);
                existingCode.setExpireTime(LocalDateTime.now().plusMinutes(5)); // 更新过期时间
                newCode = codeRepository.save(existingCode); // 保存更新
            }else{
                newCode = null;
            }

        } else {
            // 不存在现有记录，创建新记录
            newCode = new Code();
            newCode.setEmail(email);
            newCode.setCode(code);
            newCode.setExpireTime(LocalDateTime.now().plusMinutes(5)); // 设置5分钟后过期
            newCode = codeRepository.save(newCode); // 保存新记录
        }

        return newCode;
    }
}
