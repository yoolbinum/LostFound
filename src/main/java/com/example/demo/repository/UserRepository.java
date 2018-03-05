package com.example.demo.repository;


import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);
    Set<User> findUsersByRole(String role);
}
