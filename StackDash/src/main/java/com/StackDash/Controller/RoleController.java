package com.StackDash.Controller;

import com.StackDash.Entity.Role;
import com.StackDash.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/admin/")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-roles")
    public ResponseEntity<?> getAllRole(){
        try{
            List<Role> roles = roleService.getAllRole();
            if(roles.isEmpty()){
                return ResponseEntity.status(404).body("Roles not found");
            }
            return ResponseEntity.ok(roles);
        }catch (Exception ex){
            return ResponseEntity.status(500).body("Error Occurred while getting roles");
        }
    }
}
