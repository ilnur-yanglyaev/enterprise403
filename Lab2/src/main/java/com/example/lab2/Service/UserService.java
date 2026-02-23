package com.example.lab2.Service;

import com.example.lab2.Model.User;
import com.example.lab2.dto.RegisterRequest;
import com.example.lab2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
//        if (existsByUsername(request.getUsername())) {
//            throw new IllegalArgumentException("Username already exists");
//        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }








    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return UserRepository.findById(id);
    }

    public User saveUser(User User) {
        return UserRepository.save(User);
    }

    public Optional<User> updateUser(Long id, User UserDetails) {
        return UserRepository.findById(id)
                .map(existingUser -> {
                    // Обновляем поля
                    if (UserDetails.getFirstName() != null && !UserDetails.getFirstName().isBlank()) {
                        existingUser.setFirstName(UserDetails.getFirstName());
                    }
                    if (UserDetails.getLastName() != null && !UserDetails.getLastName().isBlank()) {
                        existingUser.setLastName(UserDetails.getLastName());
                    }
                    if (UserDetails.getEmail() != null && !UserDetails.getEmail().isBlank()) {
                        existingUser.setEmail(UserDetails.getEmail());
                    }
                    return UserRepository.save(existingUser);
                });
    }

    public boolean deleteUser(Long id) {
        if (UserRepository.existsById(id)) {
            UserRepository.deleteById(id);
            return true;
        }
        return false;
    }
}