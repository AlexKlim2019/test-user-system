package com.test.user.system.user.service.domain.entity;

import java.util.UUID;

public class User extends BaseEntity{
    private String username;
    private String name;
    private String surname;

    public User(UUID id, String username, String name, String surname) {
        super.setId(id);
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    private User(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setName(builder.name);
        setSurname(builder.surname);
    }

    public static Builder builder() {
        return new Builder();
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


    public static final class Builder {
        private UUID id;
        private String username;
        private String name;
        private String surname;

        private Builder() {
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder surname(String val) {
            surname = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
