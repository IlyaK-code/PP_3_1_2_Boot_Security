package ru.example.springsecurity.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.springsecurity.models.User;
import ru.example.springsecurity.repo.UserRepo;

import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return user;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> {
            return new RuntimeException(String.format("User %s not found", id));
        });
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Transactional
    public void save(User user, Collection<String> roles) {
        if (roles.contains("ROLE_ADMIN")) {
            user.setRoles(roleService.getAllRoles());
        } else {
            user.setRoles(roleService.getRoleByName("ROLE_USER"));

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Transactional
    public void update(Long id, User user, List<String> role) {
        User updatedUser = userRepo.findById(id).stream()
                .peek(up -> {
                    up.setUsername(user.getUsername());
                    up.setPassword(passwordEncoder.encode(user.getPassword()));
                    up.setEmail(user.getEmail());
                    if (role.contains("ROLE_ADMIN")) {
                        up.setRoles(roleService.getAllRoles());
                    } else {
                        up.setRoles(roleService.getRoleByName("ROLE_USER"));
                    }
                }).findFirst().get();
        userRepo.save(updatedUser);
    }

    @Transactional
    public void update(Long id, User user) {
        User updatedUser = userRepo.findById(id).stream()
                .peek(up -> {
                    up.setUsername(user.getUsername());
                    up.setPassword(passwordEncoder.encode(user.getPassword()));
                    up.setEmail(user.getEmail());
                    if (user.getAuthorities().contains("ROLE_ADMIN")) {
                        user.setRoles(roleService.getAllRoles());
                    } else {
                        user.setRoles(roleService.getRoleByName("ROLE_USER"));
                    }
                }).findFirst().get();
        userRepo.save(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
