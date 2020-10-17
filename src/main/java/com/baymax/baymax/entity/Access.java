package com.baymax.baymax.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    public long id;
    public String role;
    public String save;
    public String update;
    public String getUser;
    public String delete;
    public String read;

    @ManyToMany(mappedBy = "access", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("access")
    Set<User> users;

    public Access() {}
    public Access(String role) {
        this.role = role;
        if (this.role.equals("admin")) {
            this.save = "yes";
            this.update = "yes";
            this.getUser = "yes";
            this.delete = "yes";
            this.read = "yes";
        }
        if (this.role.equals("user")) {
            this.save = "no";
            this.update = "no";
            this.getUser = "no";
            this.delete = "no";
            this.read = "yes";
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getGetUser() {
        return getUser;
    }

    public void setGetUser(String getUser) {
        this.getUser = getUser;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User addUser) {
        if (users == null) {
            users = new HashSet<>();
        }
        this.getUsers().add(addUser);
    }

    public void removeUser(User reUser) {
        if (users == null) {
            users = new HashSet<>();
        }
        this.getUsers().remove(reUser);
    }
}
