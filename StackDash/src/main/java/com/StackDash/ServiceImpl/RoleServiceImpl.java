package com.StackDash.ServiceImpl;

import com.StackDash.Entity.Role;
import com.StackDash.Entity.RoleName;
import com.StackDash.Repository.RoleRepository;
import com.StackDash.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Set<Role> getRolesByNames(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String name : roleNames) {
            try {
                RoleName roleName = RoleName.valueOf(name.trim().toUpperCase()); // handles casing & whitespace
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + name));
                roles.add(role);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role name: " + name);
            }
        }
        return roles;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();

    }


}
