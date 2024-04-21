package com.test.user.system.user.service.domain.ports.input.service;

import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.dto.UserView;

import java.util.List;

public interface UserApplicationService {
    List<UserView> findAllUsers(SearchParams params);
}
