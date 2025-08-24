package com.example.user_management.controller;

import com.example.user_management.entity.User;
import com.example.user_management.service.UserService;
import com.example.user_management.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    // Hiển thị danh sách người dùng
    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    // Hiển thị form thêm người dùng
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "add-user";
    }

    // Xử lý thêm người dùng
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getAllCompanies());
            return "add-user";
        }
        try {
            userService.addUser(user);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage()); // Thêm thông báo lỗi
            model.addAttribute("companies", companyService.getAllCompanies()); // Giữ danh sách công ty
            return "add-user"; // Quay lại trang form
        }
    }

    // Hiển thị form sửa người dùng
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        model.addAttribute("user", user);
        model.addAttribute("companies", companyService.getAllCompanies());
        return "edit-user";
    }

    // Xử lý cập nhật người dùng
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getAllCompanies());
            return "edit-user";
        }
        try {
            user.setId(id);
            userService.updateUser(user);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "edit-user";
        }
    }

    // Xem chi tiết người dùng
    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        model.addAttribute("user", user);
        return "user-detail";
    }

    // Xóa người dùng
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}