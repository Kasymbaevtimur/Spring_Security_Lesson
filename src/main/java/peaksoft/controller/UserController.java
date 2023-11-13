package peaksoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import peaksoft.model.User;
import peaksoft.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String registration(Model model){
        model.addAttribute("user",new User());
        return "user/save";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user")User user){
        userService.save(user);
        return "redirect:/find-all";
    }

    @GetMapping("/find-all")
    public String getAllUser(Model model){
        model.addAttribute("users",userService.findAll());
        return "user/find-all";
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user",user);
        return "user/profile";
    }
    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id")Long id){
        userService.deleteById(id);
        return "redirect:/find-all";
    }

}
