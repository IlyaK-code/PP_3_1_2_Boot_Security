package ru.example.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.springsecurity.models.Role;
import ru.example.springsecurity.repo.RoleRepo;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepo.findAll());
    }

    public Set<Role> getRoleByName(String name) {
        return roleRepo.getRolesByName(name);
    }
}
