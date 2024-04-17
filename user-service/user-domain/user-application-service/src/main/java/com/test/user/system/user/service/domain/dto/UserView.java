package com.test.user.system.user.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserView {
    @NonNull
    private UUID id;
    @NonNull
    private String username;
    @NonNull
    private String name;
    @NonNull
    private String surname;
}
