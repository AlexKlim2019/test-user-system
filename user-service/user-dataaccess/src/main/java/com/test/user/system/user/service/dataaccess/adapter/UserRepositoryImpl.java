package com.test.user.system.user.service.dataaccess.adapter;

import com.test.user.system.user.service.dataaccess.repository.UserCustomRepository;
import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.entity.User;
import com.test.user.system.user.service.domain.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserCustomRepository userCustomRepository;

    @Override
    public List<User> findAllUsers(SearchParams params) {
        return userCustomRepository.findAll(params);
    }
}
