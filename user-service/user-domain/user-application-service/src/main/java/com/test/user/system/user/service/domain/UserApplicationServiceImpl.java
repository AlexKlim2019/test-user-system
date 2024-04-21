package com.test.user.system.user.service.domain;

import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.dto.UserView;
import com.test.user.system.user.service.domain.mapper.UserDataMapper;
import com.test.user.system.user.service.domain.ports.input.service.UserApplicationService;
import com.test.user.system.user.service.domain.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;

    @Override
    public List<UserView> findAllUsers(SearchParams params) {
        return userRepository.findAllUsers(params).stream()
                .map(userDataMapper::userToUserView)
                .collect(Collectors.toList());
    }
}
