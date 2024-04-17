package com.test.user.system.user.service.domain.ports.output.repository;

import com.test.user.system.user.service.domain.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAllUsers();
}
