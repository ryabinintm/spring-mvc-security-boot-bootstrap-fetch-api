package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping(path = "/")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = {"/", "login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping(path = "user")
    public String getUserList(Model model,
                              Principal principal) {
        model.addAttribute("principal", principal.getName());
        model.addAttribute("authorities", "USER");
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

}