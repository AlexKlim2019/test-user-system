package com.test.user.system.user.service.dataaccess.repository;

import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.entity.User;

import java.util.List;

public interface UserCustomRepository {
    List<User> findAll(SearchParams params);
}
