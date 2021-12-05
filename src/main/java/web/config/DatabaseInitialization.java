package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DatabaseInitialization {

    public final UserService userService;

    @Autowired
    public DatabaseInitialization(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initUsers() {
        adminUser();
        userUser();
        superUser();
    }

    private void adminUser() {
        User admin = new User(
                "James", "Smith", 31, "admin@mail.ru", "1"
        );
        Set<Role> adminRoles = Stream.of(new Role("ROLE_ADMIN"))
                .collect(Collectors.toSet());
        admin.setRoles(adminRoles);
        userService.createUser(admin);
    }

    private void userUser() {
        User user = new User(
                "Robert", "Brown", 26, "user@mail.ru", "2"
        );
        Set<Role> userRoles = Stream.of(new Role("ROLE_USER"))
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.createUser(user);
    }

    private void superUser() {
        User admin = new User(
                "William", "Johnson", 39, "super@mail.ru", "3"
        );
        Set<Role> adminRoles = Stream.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"))
                .collect(Collectors.toSet());
        admin.setRoles(adminRoles);
        userService.createUser(admin);
    }

}
