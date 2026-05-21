package com.meeting.controller;

import com.meeting.dto.ApiResult;
import com.meeting.dto.LoginRequest;
import com.meeting.entity.User;
import com.meeting.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResult<User> register(@RequestBody User user) {
        try {
            User registered = userService.register(
                    user.getUsername(), user.getPassword(),
                    user.getName(), user.getEmail(), user.getPhone());
            registered.setPassword(null);
            return ApiResult.success(registered);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResult<User> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            user.setPassword(null);
            return ApiResult.success(user);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResult.success(null);
    }

    @GetMapping("/info")
    public ApiResult<User> info(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ApiResult.error(401, "未登录");
        }
        User user = userService.findById(userId);
        user.setPassword(null);
        return ApiResult.success(user);
    }
}
