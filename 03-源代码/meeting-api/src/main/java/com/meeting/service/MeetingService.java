package com.meeting.service;

import com.meeting.entity.Meeting;

import java.util.List;

public interface MeetingService {
    List<Meeting> findAll();
    Meeting findById(Integer id);
    Meeting create(Meeting meeting);
    Meeting update(Meeting meeting);
    void delete(Integer id);
}
