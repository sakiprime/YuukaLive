package com.sakiprime.PowerfulEmpathy.service;


import com.sakiprime.PowerfulEmpathy.entity.UserEntity;

public interface LoginService {
    boolean register(UserEntity user);
    UserEntity login(Integer id, String password);
}
