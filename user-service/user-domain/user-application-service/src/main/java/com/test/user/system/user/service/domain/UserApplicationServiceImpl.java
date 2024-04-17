package com.test.user.system.user.service.domain;

import com.test.user.system.user.service.domain.dto.UserView;
import com.test.user.system.user.service.domain.mapper.UserDataMapper;
import com.test.user.system.user.service.domain.ports.input.service.UserApplicationService;
import com.test.user.system.user.service.domain.ports.output.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserRepository userRepository;

    private final UserDataMapper userDataMapper;

    public UserApplicationServiceImpl(
            UserRepository userRepository,
            UserDataMapper userDataMapper) {
        this.userRepository = userRepository;
        this.userDataMapper = userDataMapper;
    }

    @Override
    public List<UserView> findAllUsers() {
        return userRepository.findAllUsers().stream()
                .map(userDataMapper::userToUserView)
                .collect(Collectors.toList());
    }
}
