package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.dao.RoleRepository;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String getUserList(Model model,
                              Principal principal,
                              Authentication authentication,
                              @ModelAttribute("user") User user) {
        final String authorities = authentication.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        final Set<String> roleSet = roleRepository.findAll().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
        final List<String> userRoles = authentication.getAuthorities().stream().map(Objects::toString).collect(Collectors.toList());
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("principal", principal.getName());
        model.addAttribute("authorities", authorities);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleSet);
        return "admin";
    }

    @GetMapping(path = "{id}")
    public String getSingleUser(@PathVariable("id") Long id,
                                Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/user_single";
    }

    @GetMapping(path = "new")
    public String getNewUser(@ModelAttribute("user") User user) {
        return "users/create";
    }

    @PostMapping
    public String postNewUser(@ModelAttribute("user") User user,
                              @RequestParam(value = "userRoles") String[] userRoles) {
        final Set<Role> roleSet = Arrays.stream(userRoles)
                .map(Role::new)
                .collect(Collectors.toSet());
        user.setRoles(roleSet);
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping(path = "{id}/edit")
    public String getEditUser(@PathVariable("id") Long id,
                              Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/edit";
    }

    @PutMapping()
    public String putUser(@ModelAttribute("user") User user,
                          @RequestParam(value = "userRoles") String[] userRoles) {
        final Set<Role> roleSet = Arrays.stream(userRoles)
                .map(Role::new)
                .collect(Collectors.toSet());
        user.setRoles(roleSet);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(path = "{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
