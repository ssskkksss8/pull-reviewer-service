package com.example.demo.repository;

import com.example.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByTeam_TeamName(String teamName);
    List<User> findByTeam_TeamNameAndIsActiveTrue(String teamName);
}
