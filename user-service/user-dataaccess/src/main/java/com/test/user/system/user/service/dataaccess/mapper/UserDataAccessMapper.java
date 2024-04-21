package com.test.user.system.user.service.dataaccess.mapper;

import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.ColumnMapping;
import com.test.user.system.user.service.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class UserDataAccessMapper {
    public User dbRecordToUser(ColumnMapping columnMapping, Map<String, Object> dbRows) {

        return User.builder()
                .id((UUID) dbRows.get(columnMapping.getId()))
                .username((String) dbRows.get(columnMapping.getUsername()))
                .name((String) dbRows.get(columnMapping.getName()))
                .surname((String) dbRows.get(columnMapping.getSurname()))
                .build();
    }
}
