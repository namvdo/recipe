package recipes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import recipes.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value="select * from user where email = ?1", nativeQuery = true)
    Optional<User> getUserByEmail(String email);

    @Query(value="select * from user", nativeQuery = true)
    List<User> getAllUsers();
}
