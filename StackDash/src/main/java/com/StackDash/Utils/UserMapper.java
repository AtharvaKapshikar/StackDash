package com.StackDash.Utils;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Bean;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "active", target = "active", qualifiedByName = "booleanToInteger")
    GetAllUserGTO toDto(User entity);

    List<GetAllUserGTO> toDtoList(List<User> entities);

    @Named("booleanToInteger")
    default Integer mapBooleanToInteger(boolean value) {
        return value ? 1 : 0;
    }
}

