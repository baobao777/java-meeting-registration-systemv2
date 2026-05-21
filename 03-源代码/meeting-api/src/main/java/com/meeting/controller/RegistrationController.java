package com.meeting.controller;

import com.meeting.dto.ApiResult;
import com.meeting.entity.Registration;
import com.meeting.service.RegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ApiResult<Registration> register(@RequestBody Registration registration, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ApiResult.error(401, "未登录");
        }
        try {
            Registration r = registrationService.register(userId, registration.getMeetingId());
            return ApiResult.success(r);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{meetingId}")
    public ApiResult<Void> cancel(@PathVariable Integer meetingId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ApiResult.error(401, "未登录");
        }
        try {
            registrationService.cancel(userId, meetingId);
            return ApiResult.success(null);
        } catch (RuntimeException e) {
            return ApiResult.error(400, e.getMessage());
        }
    }

    @GetMapping("/my")
    public ApiResult<List<Registration>> myRegistrations(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ApiResult.error(401, "未登录");
        }
        return ApiResult.success(registrationService.findByUserId(userId));
    }

    @GetMapping("/meeting/{meetingId}")
    public ApiResult<List<Registration>> meetingRegistrations(@PathVariable Integer meetingId, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ApiResult.error(403, "无权操作");
        }
        return ApiResult.success(registrationService.findByMeetingId(meetingId));
    }

    @GetMapping("/check/{meetingId}")
    public ApiResult<Boolean> check(@PathVariable Integer meetingId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ApiResult.error(401, "未登录");
        }
        return ApiResult.success(registrationService.isRegistered(userId, meetingId));
    }
}
