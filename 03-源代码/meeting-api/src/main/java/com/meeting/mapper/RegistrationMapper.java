package com.meeting.mapper;

import com.meeting.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegistrationMapper {
    int insert(Registration registration);
    int cancel(@Param("userId") Integer userId, @Param("meetingId") Integer meetingId);
    Registration findByUserAndMeeting(@Param("userId") Integer userId, @Param("meetingId") Integer meetingId);
    List<Registration> findByUserId(@Param("userId") Integer userId);
    List<Registration> findByMeetingId(@Param("meetingId") Integer meetingId);
    int countByMeetingId(@Param("meetingId") Integer meetingId, @Param("status") String status);
}
