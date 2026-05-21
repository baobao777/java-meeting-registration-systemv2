package com.meeting.service;

import com.meeting.entity.User;

public interface UserService {
    User register(String username, String password, String name, String email, String phone);
    User login(String username, String password);
    User findById(Integer id);
}
