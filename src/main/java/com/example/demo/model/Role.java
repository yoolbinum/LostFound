package com.example.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

    @Column(unique=true)
    private String role;

    @ManyToMany(mappedBy="roles")
    private List<User> users;

    public Role() {
        this.users = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}