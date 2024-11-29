package com.ead.user_service.controller;

import com.ead.user_service.dto.UserCreateDTO;
import com.ead.user_service.dto.UserUpdateDTO;
import com.ead.user_service.dto.UserDTO;
import com.ead.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserDTO user = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(id);
        UserDTO updatedUser = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
}
