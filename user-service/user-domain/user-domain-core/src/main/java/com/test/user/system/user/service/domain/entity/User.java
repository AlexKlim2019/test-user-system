package com.test.user.system.user.service.domain.entity;

import java.util.UUID;

public class User extends BaseEntity<UUID>{
    private String username;
    private String name;
    private String surname;

    public User(UUID id, String username, String name, String surname) {
        super.setId(id);
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
