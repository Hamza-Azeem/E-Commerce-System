package com.example.producttestapi.service.impl;

import com.example.producttestapi.entities.Role;
import com.example.producttestapi.repos.RoleRepo;
import com.example.producttestapi.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }
}
