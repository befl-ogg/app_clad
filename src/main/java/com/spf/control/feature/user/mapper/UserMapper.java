package com.spf.control.feature.user.mapper;

import com.spf.control.feature.auth.domain.dto.request.UserRequest;
import com.spf.control.feature.auth.domain.dto.response.AuthenticationResponse;
import com.spf.control.feature.auth.domain.dto.response.RegisterResponse;
import com.spf.control.feature.studio.domain.entity.Studio;
import com.spf.control.feature.user.domain.entity.User;
import com.spf.control.feature.user.domain.enums.RoleType;
import com.spf.control.feature.userstudio.domain.entity.UserStudio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping( target = "accessToken", source = "tokenJWT")
    @Mapping( target = "refreshToken", source = "refreshToken")
    AuthenticationResponse toDTO(User data, String tokenJWT, String refreshToken);

    RegisterResponse toDTO(User data);

    @Mapping(target = "password", source = "password")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "role", source = "role")
    User toEntity(UserRequest request, String password, Boolean active, RoleType role);


    /*@Named("mapToUserStudios")
    default List<UserStudio> mapToUserStudios(Studio studio) {
        if (studio == null) return Collections.emptyList();
        UserStudio userStudio = new UserStudio();
        userStudio.setStudio(studio);
        return List.of(userStudio);
    }*/
}
