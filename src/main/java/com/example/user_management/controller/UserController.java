package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Hiển thị form thêm người dùng
    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    // Xử lý thêm người dùng
    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/users";
    }

    // Hiển thị danh sách người dùng
    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }
}