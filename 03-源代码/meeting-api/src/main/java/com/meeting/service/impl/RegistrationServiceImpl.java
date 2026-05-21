package com.meeting.service.impl;

import com.meeting.entity.Meeting;
import com.meeting.entity.Registration;
import com.meeting.mapper.MeetingMapper;
import com.meeting.mapper.RegistrationMapper;
import com.meeting.service.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMapper registrationMapper;
    private final MeetingMapper meetingMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper, MeetingMapper meetingMapper) {
        this.registrationMapper = registrationMapper;
        this.meetingMapper = meetingMapper;
    }

    @Override
    public Registration register(Integer userId, Integer meetingId) {
        Meeting meeting = meetingMapper.findById(meetingId);
        if (meeting == null) {
            throw new RuntimeException("会议不存在");
        }
        if (!"UPCOMING".equals(meeting.getStatus())) {
            throw new RuntimeException("会议不在报名阶段");
        }
        Registration existing = registrationMapper.findByUserAndMeeting(userId, meetingId);
        if (existing != null && "REGISTERED".equals(existing.getStatus())) {
            throw new RuntimeException("已报名该会议");
        }
        if (meeting.getCapacity() > 0) {
            int count = registrationMapper.countByMeetingId(meetingId, "REGISTERED");
            if (count >= meeting.getCapacity()) {
                throw new RuntimeException("会议名额已满");
            }
        }
        if (existing != null) {
            registrationMapper.cancel(userId, meetingId);
        }
        Registration registration = new Registration();
        registration.setUserId(userId);
        registration.setMeetingId(meetingId);
        registration.setStatus("REGISTERED");
        registrationMapper.insert(registration);
        return registration;
    }

    @Override
    public void cancel(Integer userId, Integer meetingId) {
        Registration registration = registrationMapper.findByUserAndMeeting(userId, meetingId);
        if (registration == null || !"REGISTERED".equals(registration.getStatus())) {
            throw new RuntimeException("未报名该会议");
        }
        registrationMapper.cancel(userId, meetingId);
    }

    @Override
    public List<Registration> findByUserId(Integer userId) {
        return registrationMapper.findByUserId(userId);
    }

    @Override
    public List<Registration> findByMeetingId(Integer meetingId) {
        return registrationMapper.findByMeetingId(meetingId);
    }

    @Override
    public boolean isRegistered(Integer userId, Integer meetingId) {
        Registration r = registrationMapper.findByUserAndMeeting(userId, meetingId);
        return r != null && "REGISTERED".equals(r.getStatus());
    }
}
