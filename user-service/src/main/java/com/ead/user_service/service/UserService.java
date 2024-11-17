package com.ead.user_service.service;

import com.ead.user_service.dto.UserCreateDTO;
import com.ead.user_service.dto.UserUpdateDTO;
import com.ead.user_service.dto.UserDTO;
import com.ead.user_service.model.User;
import com.ead.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setType(userCreateDTO.getType());
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setCountry(userCreateDTO.getCountry());
        user.setPhone(userCreateDTO.getPhone());
        user.setPostalCode(userCreateDTO.getPostalCode());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user = userRepository.save(user);
        return mapToUserDTO(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToUserDTO(user);
    }

    public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
        User user = findUserById(userUpdateDTO.getId());

        // validate user not found
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (userUpdateDTO.getName() != null)
            user.setName(userUpdateDTO.getName());
        if (userUpdateDTO.getEmail() != null)
            user.setEmail(userUpdateDTO.getEmail());
        if (userUpdateDTO.getCountry() != null)
            user.setCountry(userUpdateDTO.getCountry());
        if (userUpdateDTO.getPhone() != null)
            user.setPhone(userUpdateDTO.getPhone());
        if (userUpdateDTO.getPostalCode() != 0)
            user.setPostalCode(userUpdateDTO.getPostalCode());

        user.setUpdatedAt(LocalDateTime.now());

        user = userRepository.save(user);
        return mapToUserDTO(user);
    }

    public String deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        return "User deleted successfully.";

    }

    private UserDTO mapToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getType(), user.getName(), user.getEmail(),
                user.getCountry(), user.getPhone(), user.getPostalCode(),
                user.getCreatedAt().toString(), user.getUpdatedAt().toString());
    }

    private User findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user;
    }

}
