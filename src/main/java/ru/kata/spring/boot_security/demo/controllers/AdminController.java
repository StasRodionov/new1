package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping()
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin")
    public String allUsers(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("users", userService.getAllUsers());
       // model.addAttribute("user", userService.getUserByLogin(userDetails.getUsername()));
        model.addAttribute("user", user);
        //model.addAttribute("roles", roleService.getAllRoles());

        return "admin";
    }

//    @GetMapping("/admin")
//    public String showAdminPage(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("user", user);
//        model.addAttribute("roles", roleService.getAllRoles());
//        return "admin";
//    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user, @RequestParam(value = "nameRoles",required = false) String roles) {
        user.setRoles(roleService.findRolesByName(roles));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("user") User user,
                          @PathVariable("id") long id, @RequestParam(value = "nameRoles", required = false) String roles) {
        userService.getUser(id);
        user.setRoles(roleService.findRolesByName(roles));
        userService.updateUser(user);
        return "redirect:/admin";
    }

//    @PostMapping("/edit")
//    public String update(@ModelAttribute("user") User user, @RequestParam("roles") Long id) {
//        userService.updateUser(user);
//        return "redirect:/admin";
//    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}