package com.sakiprime.PowerfulEmpathy.service;

import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import com.sakiprime.PowerfulEmpathy.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private final UserMapper userMapper;
    public LoginServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public boolean register(UserEntity user){
        if(userMapper.selectById(user.getId()) != null){
            return false;
        }
    String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);
        return userMapper.insert(user)>0;
    }
    @Override
    public UserEntity login(Integer id, String password){
        UserEntity user = userMapper.selectById(id);
        if(user == null){
            return null;
        }
        if(BCrypt.checkpw(password,user.getPassword())){
            return user;
        }
        else {
            return null;
        }
    }

}
