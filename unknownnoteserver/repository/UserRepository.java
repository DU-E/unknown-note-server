package unknownnote.unknownnoteserver.repository;

import  unknownnote.unknownnoteserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
