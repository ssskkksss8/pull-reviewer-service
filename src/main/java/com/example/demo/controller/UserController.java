package com.example.demo.controller;

import com.example.demo.model.dto.UpdateUserActivityRequest;
import com.example.demo.model.dto.UserCreateRequest;
import com.example.demo.model.dto.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/by-team/{teamId}")
    public List<UserResponse> getUsersByTeam(@PathVariable UUID teamId) {
        return userService.getUsersByTeam(teamId);
    }

    @PatchMapping("/{id}/activity")
    public UserResponse updateUserActivity(
            @PathVariable UUID id,
            @RequestBody UpdateUserActivityRequest request
    ) {
        return userService.updateUserActivity(id, request);
    }
}
