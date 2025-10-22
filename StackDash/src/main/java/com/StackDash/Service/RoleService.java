package com.StackDash.Service;

import com.StackDash.Entity.Role;
import com.StackDash.Entity.RoleName;

import java.util.List;
import java.util.Set;

public interface RoleService {

    public Set<Role> getRolesByNames(Set<String> roleNames);

    public List<Role> getAllRole();

}
