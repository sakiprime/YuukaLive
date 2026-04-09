package com.sakiprime.PowerfulEmpathy.controller;

import com.sakiprime.PowerfulEmpathy.service.UploadService;
import com.sakiprime.PowerfulEmpathy.util.QiniuUtil;
import com.sakiprime.PowerfulEmpathy.util.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import cn.dev33.satoken.stp.StpUtil;
import com.sakiprime.PowerfulEmpathy.entity.UserEntity; // 你的User实体类

@RestController
@RequestMapping("/api")
public class UploadController {

    private final QiniuUtil qiniuUtil;
    private final UploadService uploadService;
    public UploadController(QiniuUtil qiniuUtil, UploadService uploadService) {
        this.qiniuUtil = qiniuUtil;
        this.uploadService = uploadService;
    }

    // 接收文件，从 Session获取当前登录用户
    @PostMapping("/uploadavatar")
    public Result<String> uploadAvatar(
            @RequestParam("file") MultipartFile file) throws Exception {
        if (!StpUtil.isLogin()) {
            return Result.fail(500, "请先登录");
        }
        UserEntity loginUser = StpUtil.getSessionByLoginId(StpUtil.getLoginId())
                .getModel("loginUser", UserEntity.class);
        String userId = loginUser.getId().toString();
        String avatarKey =qiniuUtil.uploadAvatar(file, userId);
        if(!uploadService.uploadAvatar(loginUser, avatarKey)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        StpUtil.getSessionByLoginId(doneUpdateUser.getId()).set("loginUser", doneUpdateUser);

        return Result.success(avatarKey);
    }
    @PostMapping("/uploadresume")
    public Result<String> uploadResume(
            @RequestParam("file") MultipartFile file) throws Exception {
        if (!StpUtil.isLogin()) {
            return Result.fail(500,"请先登录");
        }
        UserEntity loginUser = StpUtil.getSession().getModel("loginUser", UserEntity.class);
        String userId = loginUser.getId().toString();
        String resumeKey =qiniuUtil.uploadResume(file, userId);
        if(!uploadService.uploadResume(loginUser, resumeKey)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        StpUtil.getSession().set("loginUser", doneUpdateUser);

        return Result.success(resumeKey);
    }
    @PutMapping("/uploadinfo")
    public Result<Void> uploadInfo(@RequestBody UserEntity user) throws Exception {
        if (!StpUtil.isLogin()) {
            return Result.fail(500,"请先登录");
        }
        UserEntity loginUser = StpUtil.getSession().getModel("loginUser", UserEntity.class);
        user.setId(loginUser.getId());
        if(!uploadService.uploadInfo(user)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        StpUtil.getSession().set("loginUser",doneUpdateUser); //重载session。
        return Result.success(null);
    }
    @GetMapping("/downloadinfo")
    public Result<UserEntity> downloadInfo() {
        // 1. 先判断是否登录（必须）
        if (!StpUtil.isLogin()) {
            return Result.fail(500, "请先登录");
        }

        // 2. 从当前登录会话中取出用户（最标准写法）
        UserEntity loginUser = StpUtil.getSessionByLoginId(StpUtil.getLoginId())
                .getModel("loginUser", UserEntity.class);
        // 3. 安全处理
        loginUser.setPassword(null);

        return Result.success(loginUser);
    }
}