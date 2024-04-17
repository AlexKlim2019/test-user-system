package com.test.user.system.user.service.domain.mapper;

import com.test.user.system.user.service.domain.dto.UserView;
import com.test.user.system.user.service.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {
    public UserView userToUserView(User user) {
        return UserView.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }
}
