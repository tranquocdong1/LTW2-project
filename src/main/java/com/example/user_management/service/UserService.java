package com.example.user_management.service;

import com.example.user_management.entity.User;
import com.example.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Thêm người dùng mới
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}