package com.test.user.system.user.service.dataaccess.repository;

import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties;
import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.ColumnMapping;
import com.test.user.system.user.service.dataaccess.config.DatabaseConnectionProperties.DatabaseProps;
import com.test.user.system.user.service.dataaccess.mapper.UserDataAccessMapper;
import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{
    private static final String PREFIX_SQL_QUERY = "SELECT * FROM %s ";

    private final DatabaseConnectionProperties dbConnections;
    private final UserDataAccessMapper userDataAccessMapper;

    @Override
    public List<User> findAll(SearchParams params) {
        return dbConnections.getDataSources().stream()
                .map(databaseProps -> findAllInSeparateDb(databaseProps, params))
                .flatMap(List::stream)
                .collect(toList());
    }

    // TODO It's better to start all database and use them in parallel
    private List<User> findAllInSeparateDb(DatabaseProps dbProps, SearchParams params) {
        var paramSource = buildParameterSource(dbProps.getMapping(), params);

        var dataSource = buildDataSource(dbProps);
        var jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        var sqlPrefix = buildSqlPrefix(dbProps.getTable());
        var rows = jdbcTemplate
                .query(sqlPrefix, paramSource, new ColumnMapRowMapper()); // TODO make the sql query more save
        return rows
                .stream()
                .map(dbRows -> userDataAccessMapper.dbRecordToUser(dbProps.getMapping(), dbRows))
                .collect(toList());
    }

    private MapSqlParameterSource buildParameterSource(ColumnMapping columnNames, SearchParams params) {
        var result = new MapSqlParameterSource();
        if (isNotBlank(params.name())) {
            result.addValue(columnNames.getName(), params.name());
        }
        if (isNotBlank(params.username())) {
            result.addValue(columnNames.getUsername(), params.username());
        }
        if (isNotBlank(params.surname())) {
            result.addValue(columnNames.getSurname(), params.surname());
        }
        return result;
    }

    private DataSource buildDataSource(DatabaseProps props) {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(props.getUrl())
                .username(props.getUser())
                .password(props.getPassword())
                .build();
    }

    private String buildSqlPrefix(String tableName) {
        return String.format(PREFIX_SQL_QUERY, tableName);
    }
}
