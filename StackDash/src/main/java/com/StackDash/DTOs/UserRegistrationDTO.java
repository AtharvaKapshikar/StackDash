package com.StackDash.DTOs;

import com.StackDash.Entity.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private Integer age;
    private String city;
    private String mobileNumber;
    private String email;
    private String designation;
    private String userName;
    private String password;
    private Set<Role> roles;
}
