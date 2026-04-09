package com.sakiprime.PowerfulEmpathy.controller;

import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import com.sakiprime.PowerfulEmpathy.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import com.sakiprime.PowerfulEmpathy.util.Result;

@RestController
//@RequestMapping("/api") login相关为一级路径
public class LoginController {
    private final LoginService loginService;
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody UserEntity user){

    boolean registered = loginService.register(user);
    return registered?Result.success(null):Result.fail(500,"用户已存在");
    }
    @PostMapping("/login")
    public Result<Void> login(@RequestBody UserEntity unCheckedUser, HttpSession session){

    UserEntity userdata = loginService.login(unCheckedUser.getId(), unCheckedUser.getPassword());
    if(userdata == null){
        return Result.fail(500,"账号或密码错误");
    }
    session.setAttribute("loginUser", userdata);

    return Result.success(null);
    }
    @GetMapping("/checkLogin")
    public Result<Void> checkLogin(HttpSession session) {

        UserEntity loginUser = (UserEntity) session.getAttribute("loginUser");
        if(loginUser == null){
            return Result.fail();
        }
        return Result.success(null);
    }
    @GetMapping("/logout")
    public Result<Void> logout(HttpSession session) {

        session.invalidate(); // 清空Session
        return Result.success("退出登录成功",null);
    }
}
