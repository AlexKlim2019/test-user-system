package com.test.user.system.user.service.application.rest;

import com.test.user.system.user.service.domain.dto.SearchParams;
import com.test.user.system.user.service.domain.dto.UserView;
import com.test.user.system.user.service.domain.ports.input.service.UserApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = "application/vnd.api.v1+json")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<UserView>> findAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname
    ) {
        var searchParams = new SearchParams(username, name, surname);
        var users = userApplicationService.findAllUsers(searchParams);
        return ResponseEntity.ok(users);
    }
}
