package com.meeting.controller;

import com.meeting.dto.ApiResult;
import com.meeting.entity.Meeting;
import com.meeting.service.MeetingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/list")
    public ApiResult<List<Meeting>> list() {
        return ApiResult.success(meetingService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResult<Meeting> detail(@PathVariable Integer id) {
        try {
            return ApiResult.success(meetingService.findById(id));
        } catch (RuntimeException e) {
            return ApiResult.error(404, e.getMessage());
        }
    }

    @PostMapping
    public ApiResult<Meeting> create(@RequestBody Meeting meeting, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ApiResult.error(403, "无权操作");
        }
        meeting.setCreatorId((Integer) session.getAttribute("userId"));
        meeting.setStatus("UPCOMING");
        return ApiResult.success(meetingService.create(meeting));
    }

    @PutMapping("/{id}")
    public ApiResult<Meeting> update(@PathVariable Integer id, @RequestBody Meeting meeting, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ApiResult.error(403, "无权操作");
        }
        meeting.setId(id);
        try {
            return ApiResult.success(meetingService.update(meeting));
        } catch (RuntimeException e) {
            return ApiResult.error(404, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Integer id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            return ApiResult.error(403, "无权操作");
        }
        try {
            meetingService.delete(id);
            return ApiResult.success(null);
        } catch (RuntimeException e) {
            return ApiResult.error(404, e.getMessage());
        }
    }
}
