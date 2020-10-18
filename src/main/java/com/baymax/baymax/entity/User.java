package com.baymax.baymax.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String username;
    private String password;
    private String role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permission",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "access_id") })
    @JsonIgnoreProperties("users")
    private Set<Access> access;
    public User() {}


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Set<Access> getAccess() {
        return access;
    }

    public void setAccess(Set<Access> access) {
        this.access = access;
    }

    public void addAccess(Access addAccess) {
        if (access == null) {
            access = new HashSet<>();
        }
        this.getAccess().add(addAccess);
    }

    public void removeAccess(Access reAccess) {
        if (access == null) {
            access = new HashSet<>();
        }
        this.getAccess().remove(reAccess);
    }
    public List<User> getFromAccess(List<Access> access) {
        List<User> users = new ArrayList<>();
        for (Access per: access) {
            Set<User> user = per.getUsers();
            users.add(user.iterator().next());
        }
        return users;
    }

}