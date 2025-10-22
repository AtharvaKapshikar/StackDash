package com.StackDash.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "StackDash_Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // ✅ Corrected generator name
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name; // e.g., USER, ADMIN

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    // Constructors

    public Role(){

    }
    public Role(RoleName name) {
        this.name = name;
    }


    public Role(Long id, RoleName name, Set<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                ", users=" + users +
                '}';
    }
}