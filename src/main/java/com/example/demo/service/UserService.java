package com.example.demo.service;

import com.example.demo.model.dto.UserResponse;
import com.example.demo.model.entity.User;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByTeam(UUID teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
        return userRepository.findByTeam_Id(teamId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUserActivity(String userId, Boolean active) {
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.setActive(active);
        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId().toString())
                .username(user.getName())
                .teamName(user.getTeam().getName())
                .active(user.isActive())
                .build();
    }
}
