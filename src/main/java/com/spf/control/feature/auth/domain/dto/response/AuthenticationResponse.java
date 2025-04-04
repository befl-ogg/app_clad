package com.spf.control.feature.auth.domain.dto.response;

import com.spf.control.feature.user.domain.enums.RoleType;

public record AuthenticationResponse(Long id, String name, String email,
                                     RoleType role, String accessToken,
                                     String refreshToken) {
}
