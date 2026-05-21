package com.meeting.mapper;

import com.meeting.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingMapper {
    List<Meeting> findAll();
    Meeting findById(@Param("id") Integer id);
    int insert(Meeting meeting);
    int update(Meeting meeting);
    int deleteById(@Param("id") Integer id);
    int countByStatus(@Param("status") String status);
}
