package ru.example.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.springsecurity.models.User;
import ru.example.springsecurity.service.RoleService;
import ru.example.springsecurity.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String adminHomeAndAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @PostMapping
    public String adminHomePost(@RequestParam("id") Long id, @RequestParam("action") String action) {
        User user = userService.findById(id);
        if (action.equals("delete")) {
            userService.delete(user.getId());
        } else if (action.equals("edit")) {
            return "redirect:/admin/update?id=" + user.getId();
        }
        return "redirect:/admin";
    }


    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("authorities") List<String> roles) {
        userService.save(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String edit(Model model, @RequestParam("id") Long id) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userRole", user.getAuthorities());
        model.addAttribute("isAdmin", user.getAuthorities().contains("ROLE_ADMIN"));
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") Long id, @RequestParam("role") List<String> roles) {
        userService.update(id, user, roles);
        return "redirect:/admin";
    }
}
