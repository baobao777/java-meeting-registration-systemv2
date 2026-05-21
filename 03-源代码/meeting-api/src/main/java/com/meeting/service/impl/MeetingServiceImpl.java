package com.meeting.service.impl;

import com.meeting.entity.Meeting;
import com.meeting.mapper.MeetingMapper;
import com.meeting.service.MeetingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final MeetingMapper meetingMapper;

    public MeetingServiceImpl(MeetingMapper meetingMapper) {
        this.meetingMapper = meetingMapper;
    }

    @Override
    public List<Meeting> findAll() {
        return meetingMapper.findAll();
    }

    @Override
    public Meeting findById(Integer id) {
        Meeting meeting = meetingMapper.findById(id);
        if (meeting == null) {
            throw new RuntimeException("会议不存在");
        }
        return meeting;
    }

    @Override
    public Meeting create(Meeting meeting) {
        meetingMapper.insert(meeting);
        return meeting;
    }

    @Override
    public Meeting update(Meeting meeting) {
        findById(meeting.getId());
        meetingMapper.update(meeting);
        return meetingMapper.findById(meeting.getId());
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        meetingMapper.deleteById(id);
    }
}
