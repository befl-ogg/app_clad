package com.spf.control.feature.auth.domain.dto.request;

import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @Size(min = 1, max = 20)
        String userName,
        @Size(min = 4, max = 50)
        String password)  {
}