package com.baymax.baymax.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String username;
    private String password;
    private String role;
    private String symptom;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permission",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "accessMethod_id") })
    @JsonIgnoreProperties("users")
    private Set<AccessMethod> accessMethods;

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
    public String getSymptom() {
        return symptom;
    }
    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public Set<AccessMethod> getAccessMethods() {
        return accessMethods;
    }

    public void setAccessMethods(Set<AccessMethod> accessMethods) {
        this.accessMethods = accessMethods;
    }

    public void addAccessMethod(AccessMethod method) {
        if (accessMethods == null) {
            accessMethods = new HashSet<>();
        }
        this.getAccessMethods().add(method);
    }

    public void removeAccessMethod(AccessMethod method) {
        if (accessMethods == null) {
            accessMethods = new HashSet<>();
        }
        this.getAccessMethods().remove(method);
    }
}