package com.test.user.system.user.service.dataaccess.repository;

import com.test.user.system.user.service.dataaccess.config.DBContextHolder;
import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties;
import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.ColumnMapping;
import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.DatabaseProps;
import com.test.user.system.user.service.dataaccess.mapper.UserDataAccessMapper;
import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.test.user.system.user.service.dataaccess.config.SqlConstants.*;
import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final DatabaseConnectionProperties dbConnections;
    private final UserDataAccessMapper userDataAccessMapper;
    private final DataSource dataSource;

    @Override
    public List<User> findAll(SearchParams params) {
        return dbConnections.getDataSources().stream()
                .map(databaseProps -> findAllInSeparateDb(databaseProps, params))
                .flatMap(List::stream)
                .collect(toList());
    }

    private List<User> findAllInSeparateDb(DatabaseProps dbProps, SearchParams params) {
        DBContextHolder.setCurrentDb(dbProps.getName());
        var jdbcTemplate = new JdbcTemplate(dataSource);
        var sql = buildSqlPrefix(dbProps.getTable())
                .append(buildFilters(dbProps.getMapping(), params))
                .toString();
        return jdbcTemplate.query(sql, new ColumnMapRowMapper())
                .stream()
                .map(dbRows -> userDataAccessMapper.dbRecordToUser(dbProps.getMapping(), dbRows))
                .collect(toList());
    }

    private StringBuilder buildSqlPrefix(String tableName) {
        return new StringBuilder(PREFIX_SQL_QUERY).append(tableName);
    }

    private StringBuilder buildFilters(ColumnMapping columnNames, SearchParams params) {
        var filters = new ArrayList<String>();
        if (isNotBlank(params.name())) {
            filters.add(String.format(FILTER_TEMPLATE, columnNames.getName(), params.name()));
        }
        if (isNotBlank(params.username())) {
            filters.add(String.format(FILTER_TEMPLATE, columnNames.getUsername(), params.username()));
        }
        if (isNotBlank(params.surname())) {
            filters.add(String.format(FILTER_TEMPLATE, columnNames.getSurname(), params.surname()));
        }

        return filters.stream()
                .map(StringBuilder::new)
                .reduce(new StringBuilder(), (acc, cur) -> {
                    if (acc.isEmpty()) {
                        acc.append(WHERE_OPERATOR);
                    } else {
                        acc.append(AND_OPERATOR);
                    }
                    return acc.append(cur);
                });
    }
}
