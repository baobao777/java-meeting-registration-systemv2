package com.meeting.service;

import com.meeting.entity.Registration;

import java.util.List;

public interface RegistrationService {
    Registration register(Integer userId, Integer meetingId);
    void cancel(Integer userId, Integer meetingId);
    List<Registration> findByUserId(Integer userId);
    List<Registration> findByMeetingId(Integer meetingId);
    boolean isRegistered(Integer userId, Integer meetingId);
}
