package com.scm.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.scm.entities.User;
@Repository
public interface  UserRepository extends JpaRepository<User,String>{
//extra methods db releated operations

//custom query methods

//custom finder methods
Optional<User> findByEmail(String email);
Optional<User>findByEmailAndPassword(String email,String password);
User findByEmailIgnoreCase(String email);

}
