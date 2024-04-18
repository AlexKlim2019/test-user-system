package com.test.user.system.user.service.dataaccess.adapter;

import com.test.user.system.user.service.dataaccess.mapper.UserDataAccessMapper;
import com.test.user.system.user.service.dataaccess.repository.UserJpaRepository;
import com.test.user.system.user.service.domain.entity.User;
import com.test.user.system.user.service.domain.ports.output.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDataAccessMapper userDataAccessMapper;

    public UserRepositoryImpl(
            UserJpaRepository userJpaRepository,
            UserDataAccessMapper userDataAccessMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userDataAccessMapper = userDataAccessMapper;
    }

    @Override
    public List<User> findAllUsers() {
        return userJpaRepository.findAll().stream()
                .map(userDataAccessMapper::userEntityToUser)
                .collect(Collectors.toList());
    }
}
