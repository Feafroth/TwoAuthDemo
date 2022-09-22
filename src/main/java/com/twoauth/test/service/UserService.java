package com.twoauth.test.service;

import com.twoauth.test.entity.Role;
import com.twoauth.test.entity.User;
import com.twoauth.test.repository.RoleRepository;
import com.twoauth.test.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        User storedUser = userRepository.findByLogin(user.getLogin());
        if (storedUser == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        storedUser.setRoles(user.getRoles());
        storedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(storedUser);
    }

    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

}