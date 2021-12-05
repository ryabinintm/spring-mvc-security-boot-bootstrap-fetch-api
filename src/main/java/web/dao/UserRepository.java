package web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    @Query("SELECT u FROM User u INNER JOIN fetch u.roles WHERE u.email = :email")
    User findUserByEmail(String email);

}
