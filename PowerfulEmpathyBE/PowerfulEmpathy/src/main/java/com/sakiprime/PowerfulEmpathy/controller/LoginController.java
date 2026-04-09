package com.sakiprime.PowerfulEmpathy.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sakiprime.PowerfulEmpathy.entity.UserEntity;
import com.sakiprime.PowerfulEmpathy.service.LoginService;
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
    public Result<Void> login(@RequestBody UserEntity unCheckedUser) {

        UserEntity userdata = loginService.login(unCheckedUser.getId(), unCheckedUser.getPassword());
        if(userdata == null){
            return Result.fail(500,"账号或密码错误");
        }

        StpUtil.login(userdata.getId());

        StpUtil.getSessionByLoginId(userdata.getId()).set("loginUser", userdata);

        return Result.success(null);
    }
    @GetMapping("/checkLogin")
    public Result<Void> checkLogin() {
        // 一行判断是否登录
        if (!StpUtil.isLogin()) {
            return Result.fail();
        }
        return Result.success(null);
    }
    @GetMapping("/logout")
    public Result<Void> logout() {
        // Sa-Token 退出：自动清除会话、Cookie、Redis/内存
        StpUtil.logout();
        return Result.success("退出登录成功", null);
    }
}
