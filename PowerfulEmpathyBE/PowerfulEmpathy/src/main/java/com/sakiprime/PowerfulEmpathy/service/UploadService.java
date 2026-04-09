package com.sakiprime.PowerfulEmpathy.service;

import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    boolean uploadAvatar(UserEntity loginUser,String avatarKey);
    boolean uploadInfo(UserEntity toUpdateUser);
    UserEntity getUserById(Integer id);
    boolean uploadResume(UserEntity loginUser,String resumeKey);
}
