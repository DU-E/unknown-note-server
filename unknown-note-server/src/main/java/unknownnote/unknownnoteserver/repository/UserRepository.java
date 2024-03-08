package unknownnote.unknownnoteserver.repository;

import org.springframework.stereotype.Repository;
import  unknownnote.unknownnoteserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
