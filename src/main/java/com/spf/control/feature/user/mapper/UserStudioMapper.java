package com.spf.control.feature.user.mapper;

import com.spf.control.feature.studio.domain.entity.Studio;
import com.spf.control.feature.user.domain.entity.User;
import com.spf.control.feature.userstudio.domain.entity.UserStudio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStudioMapper {
    UserStudioMapper INSTANCE = Mappers.getMapper(UserStudioMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", source = "active")
    UserStudio toEntity (User user, Studio studio, Boolean active);
}
