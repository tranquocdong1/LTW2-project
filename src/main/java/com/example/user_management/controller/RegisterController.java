package com.example.user_management.controller;

import com.example.user_management.entity.Role;
import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            // Kiểm tra email đã tồn tại
            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                model.addAttribute("errorMessage", "Email đã tồn tại!");
                return "register";
            }

            // Gán role mặc định
            user.setRole(Role.USER);

            // Lưu user (service sẽ encode password)
            userService.addUser(user);

            return "redirect:/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi đăng ký.");
            return "register";
        }
    }
}
