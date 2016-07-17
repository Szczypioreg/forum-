/**
 * Created by Dawid Stankiewicz on 10.07.2016
 */
package com.github.szczypioreg.forum.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.szczypioreg.forum.domain.User;
import com.github.szczypioreg.forum.service.UserService;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/user/{username}")
    public String getUserProfileByUsername(Model model, @PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }
    
    @RequestMapping("/user/id/{id}")
    public String getUserProfileById(Model model, @PathVariable("id") int id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user";
    }
    
    @RequestMapping("/users")
    public String allUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String regiristrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }
    
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String regiristrationNewUser(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/user/" + user.getUsername();
    }
    
    @RequestMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            
        }
        return "redirect:/login?logout=true";
    }
    
}
