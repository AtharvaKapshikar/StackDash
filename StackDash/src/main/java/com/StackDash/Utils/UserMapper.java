package com.StackDash.Utils;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.Entity.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    GetAllUserGTO toDto(User entity);

    List<GetAllUserGTO> toDtoList(List<User> entities);

}

