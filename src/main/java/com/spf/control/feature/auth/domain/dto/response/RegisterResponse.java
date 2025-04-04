package com.spf.control.feature.auth.domain.dto.response;

import com.spf.control.feature.user.domain.enums.RoleType;

public record RegisterResponse(String name, String email,
                               RoleType role) {
}
