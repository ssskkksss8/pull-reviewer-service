package com.example.demo.service;

import com.example.demo.model.dto.UpdateUserActivityRequest;
import com.example.demo.model.dto.UserCreateRequest;
import com.example.demo.model.dto.UserResponse;
import com.example.demo.model.entity.Team;
import com.example.demo.model.entity.User;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + request.getTeamId()));

        User user = User.builder()
                .name(request.getName())
                .isActive(true)
                .team(team)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    @Transactional
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return mapToResponse(user);
    }

    @Transactional
    public List<UserResponse> getUsersByTeam(UUID teamId) {
        List<User> users = userRepository.findByTeam_Id(teamId);
        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateUserActivity(UUID userId, UpdateUserActivityRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setActive(request.isActive());
        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .isActive(user.isActive())
                .teamId(user.getTeam().getId())
                .build();
    }
}
