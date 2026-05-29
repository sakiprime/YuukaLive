package com.sakiprime.DrivenFear.service.userfile;

import com.sakiprime.DrivenFear.common.util.Result;
import com.sakiprime.DrivenFear.entity.UserDTO;
import com.sakiprime.DrivenFear.entity.UserEntity;

import java.util.List;

public interface UserCommonService {
    boolean uploadAvatar(String userId, String avatarKey);
    Result<Void> uploadInfo(UserDTO toUpdateUser);
    UserEntity getUserById(String id);
    List<UserEntity> downloadLongLoginUser();
    Result<UserEntity> refreshUserSession(String userId);
    Result<Void> handleSign(String userId);
}
