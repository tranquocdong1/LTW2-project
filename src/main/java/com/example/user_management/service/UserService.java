package com.example.user_management.service;

import com.example.user_management.entity.User;
import com.example.user_management.entity.Company;
import com.example.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Thêm người dùng mới
    public User addUser(User user) {
        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        // Mã hóa password trước khi lưu (nếu cần)
        user.setPassword(passwordEncoder.encode(user.getPassword())); //
        return userRepository.save(user);
    }

    // Lấy danh sách tất cả người dùng
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy người dùng theo ID
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Lấy users không thuộc company nào
    @Transactional(readOnly = true)
    public List<User> getUsersWithoutCompany() {
        return userRepository.findUsersWithoutCompany();
    }

    // Cập nhật người dùng
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update");
        }

        // Kiểm tra user có tồn tại không
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + user.getId()));

        // Kiểm tra email conflict (nếu email thay đổi)
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
        }

        return userRepository.save(user);
    }

    // Xóa người dùng
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}