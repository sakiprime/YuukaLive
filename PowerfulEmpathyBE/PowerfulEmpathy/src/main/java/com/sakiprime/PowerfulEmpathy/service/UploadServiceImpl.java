package com.sakiprime.PowerfulEmpathy.service;

import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import com.sakiprime.PowerfulEmpathy.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {
    private final UserMapper userMapper;
    public UploadServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public boolean uploadAvatar(UserEntity loginUser,String avatarKey) {//更新数据库对象。
    loginUser.setAvatarKey(avatarKey);
    return userMapper.updateById(loginUser)>0;
    }
    @Override
    public boolean uploadInfo(UserEntity toUpdateUser){
        return userMapper.updateById(toUpdateUser)>0;
    }
    @Override
    public UserEntity getUserById(Integer id){
        return userMapper.selectById(id);
    }
    @Override
    public boolean uploadResume(UserEntity loginUser,String resumeKey) {
        loginUser.setResumeKey(resumeKey);
        return userMapper.updateById(loginUser)>0;
    }
}
