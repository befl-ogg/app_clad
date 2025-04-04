package com.spf.control.feature.auth.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull(message = "Username can't be null")
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 1, max = 20, message = "Username must be between 1 and 20 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name can't be empty")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @NotNull(message = "Last name can't be null")
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @NotNull(message = "Role type can't be null")
    @NotEmpty(message = "Role type can't be empty")
    @Size(min = 5, max = 14, message = "ole type must be between 5 and 14 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roleType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Active can't be null")
    private Boolean active;

    @NotNull(message = "Studio code can't be null")
    @NotEmpty(message = "Studio code can't be empty")
    @Size(min = 1, max = 20, message = "Studio code must be between 1 and 20 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String studioCode;
}
