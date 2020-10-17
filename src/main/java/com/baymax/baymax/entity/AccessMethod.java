package com.baymax.baymax.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class AccessMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)


    private long id;
    private String method;

    @ManyToMany(mappedBy = "accessMethods", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("accessMethods")
    Set<User> users;

    public AccessMethod() {}
    public AccessMethod(String method, Set<User> users) {
        this.method = method;
        this.users = users;
    }

}
