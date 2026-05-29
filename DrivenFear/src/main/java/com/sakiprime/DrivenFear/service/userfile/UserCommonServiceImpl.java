package com.sakiprime.DrivenFear.service.userfile;

import cn.dev33.satoken.stp.StpUtil;
import com.sakiprime.DrivenFear.common.util.Result;
import com.sakiprime.DrivenFear.common.util.TimeUtil;
import com.sakiprime.DrivenFear.entity.UserDTO;
import com.sakiprime.DrivenFear.entity.UserEntity;
import com.sakiprime.DrivenFear.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommonServiceImpl implements UserCommonService {
    private final UserMapper userMapper;
    private final RedisTemplate<String, String> redisTemplate;
    //常量。真是优雅。双倍魔法数字给下一个人。
    private static final int SIGN_REWARD_TOKENS = 10;
    @Override
    public boolean uploadAvatar(String userId, String avatarKey) {//更新数据库对象。
        if(userMapper.updateAvatarKeyAtomic(userId,avatarKey)==0){
            log.error("原子化更新头像地址失败，用户ID:{}",userId);
            return false;
        }
        return true;
    }
    @Override
    public Result<Void> uploadInfo(UserDTO toUpdateUser){
        String userId = toUpdateUser.getUserId();
        String username = toUpdateUser.getUsername();
        if (username != null && !username.isBlank()) {
            if (!username.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,20}$")) {
                return Result.fail(400,"用户名格式错误：支持20字内中文、字母、数字，不允许特殊字符");
            }
            if(userMapper.updateUsernameAtomic(userId, username)==0){
                log.error("原子化更新用户名失败，用户ID:{}",userId);
                return Result.fail(500,"系统繁忙，请稍后再试。");
            }
        }
        String phone = toUpdateUser.getPhone();
        if (phone != null && !phone.isBlank()) {
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.fail(400,"手机号格式不正确,仅支持中国大陆运营商手机号");
            }
            if(userMapper.updatePhoneAtomic(userId, phone)==0){
                log.error("原子化更新手机号失败，用户ID:{}",userId);
                return Result.fail(500,"系统繁忙，请稍后再试。");
            }
        }

        return Result.success(null);
    }
    @Override
    public UserEntity getUserById(String id){
        //其实在Controller已经去敏过一次咯！
        UserEntity user =userMapper.selectById(id);
        user.setPassword(null);
        return user;
    }
    @Override
    public List<UserEntity> downloadLongLoginUser(){
        List<UserEntity> list = userMapper.selectList(null);
        for (UserEntity user : list) {
            user.setPassword(null);
        }
        return list;
    }
    //接受用户ID，从数据库刷新其信息至缓存。
    @Override
    public Result<UserEntity> refreshUserSession(String userId){

        UserEntity doneUpdateUser = getUserById(userId);
        if(doneUpdateUser==null){
            log.error("session缓存刷新失败，用户ID:{}",userId);
            return Result.fail();
        }
        doneUpdateUser.setPassword(null);
        StpUtil.getSessionByLoginId(userId)
                .set("loginUser", doneUpdateUser);
        return Result.success(doneUpdateUser);
    }

    @Override
    public Result<Void> handleSign(String userId){

        String monthDate = TimeUtil.nowMonth();
        long onlyDay = Long.parseLong(TimeUtil.nowOnlyDay()) ;
        String signKey = "sign:user:" + userId + ":month:" + monthDate;
        try {//可能返回null，要用Boolean包装类。先设置签到状态，杜绝刷代币风险。
            Boolean signed = redisTemplate.opsForValue().setBit(signKey, onlyDay, true);
            if (Boolean.TRUE.equals(signed)) {
                return Result.fail(409,"今日已签到，请勿重复操作");
            }
            if(userMapper.increaseTokenBalanceAtomic(userId,SIGN_REWARD_TOKENS)==0){
                log.warn("用户签到增加token失败。用户ID:{}",userId);
                redisTemplate.opsForValue().setBit(signKey, onlyDay, false);
                //可能没什么必要。但本就是极端场景性能开销很少。
                if (Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(signKey, onlyDay))) {
                    log.error("[需要人工核查]用户签到状态重置失败。用户ID:{}",userId);
                }
                return Result.fail(500, "系统繁忙，签到失败。");
            }
        } catch (Exception e) {
            log.error("用户签到Redis写入异常，userId:{}", userId, e);
            return Result.fail(500, "系统繁忙，签到失败。");
        }
        return Result.success("签到成功。",null);
    }
}
