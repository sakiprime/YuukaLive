package com.sakiprime.PowerfulEmpathy.controller;

import com.sakiprime.PowerfulEmpathy.service.UploadService;
import com.sakiprime.PowerfulEmpathy.util.QiniuUtil;
import com.sakiprime.PowerfulEmpathy.util.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
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
            @RequestParam("file") MultipartFile file, HttpSession session) throws Exception {

        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.fail();
        }
        String userId = loginUser.getId().toString();
        String avatarKey =qiniuUtil.uploadAvatar(file, userId);
        if(!uploadService.uploadAvatar(loginUser, avatarKey)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        session.setAttribute("loginUser", doneUpdateUser);

        return Result.success(avatarKey);
    }
    @PostMapping("/uploadresume")
    public Result<String> uploadResume(
            @RequestParam("file") MultipartFile file, HttpSession session) throws Exception {

        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.fail();
        }//检测用户登录。
        String userId = loginUser.getId().toString();
        String resumeKey =qiniuUtil.uploadResume(file, userId);
        if(!uploadService.uploadResume(loginUser, resumeKey)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        session.setAttribute("loginUser", doneUpdateUser);

        return Result.success(resumeKey);
    }
    @PutMapping("/uploadinfo")
    public Result<Void> uploadInfo(@RequestBody UserEntity user, HttpSession session) throws Exception {
        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.fail(500,"请先登录");
        }
        user.setId(loginUser.getId());
        if(!uploadService.uploadInfo(user)){
            return Result.fail();
        }
        UserEntity doneUpdateUser = uploadService.getUserById(loginUser.getId());
        session.setAttribute("loginUser", doneUpdateUser);//重载session。
        return Result.success(null);
    }
    @GetMapping("/downloadinfo")
    public Result<UserEntity> downloadInfo(HttpSession session){
        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.fail(500,"请先登录");
        }
        loginUser.setPassword(null);//剔除密码。
        return Result.success(loginUser);
    }
}