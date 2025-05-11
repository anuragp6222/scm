package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
//(hum user repo ko directly apne controller me use kar sakte hai... pr yeh standard nahi hai uske liy services banayenge then hum user repo ko services me inject karenge aur services ko controller me)
    //extra methods joh bhi chahiye
    //custom query methods
    //custom finder methods

    //findByEmail ke liye koi method tha nahi ab yaha banayenge
    Optional<User> findByEmail(String email); //iski implementation likha nahi JPA apne aap dekh lega follow camel case
    //Optional<User> findByEmailAndPassword(String email,String password);
}
