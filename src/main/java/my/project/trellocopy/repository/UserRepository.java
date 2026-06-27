package my.project.trellocopy.repository;

import my.project.trellocopy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        select u from User u where u.email = :username and u.deleted = false
""")
    Optional<User> findUserByEmail(String username);

    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);
}
