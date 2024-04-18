package com.test.user.system.user.service.dataaccess.mapper;

import com.test.user.system.user.service.dataaccess.entity.UserEntity;
import com.test.user.system.user.service.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDataAccessMapper {
    public User userEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .build();
    }
}
