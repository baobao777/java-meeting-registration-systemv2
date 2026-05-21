package com.meeting.entity;

import java.time.LocalDateTime;

public class Registration {
    private Integer id;
    private Integer userId;
    private Integer meetingId;
    private LocalDateTime registeredAt;
    private String status; // REGISTERED, CANCELLED

    // not columns
    private String meetingTitle;
    private String meetingLocation;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
    private String userName;
    private String userPhone;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getMeetingId() { return meetingId; }
    public void setMeetingId(Integer meetingId) { this.meetingId = meetingId; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMeetingTitle() { return meetingTitle; }
    public void setMeetingTitle(String meetingTitle) { this.meetingTitle = meetingTitle; }
    public String getMeetingLocation() { return meetingLocation; }
    public void setMeetingLocation(String meetingLocation) { this.meetingLocation = meetingLocation; }
    public LocalDateTime getMeetingStartTime() { return meetingStartTime; }
    public void setMeetingStartTime(LocalDateTime meetingStartTime) { this.meetingStartTime = meetingStartTime; }
    public LocalDateTime getMeetingEndTime() { return meetingEndTime; }
    public void setMeetingEndTime(LocalDateTime meetingEndTime) { this.meetingEndTime = meetingEndTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
}
