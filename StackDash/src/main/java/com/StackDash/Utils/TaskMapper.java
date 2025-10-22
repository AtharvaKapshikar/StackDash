package com.StackDash.Utils;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.DTOs.GetTaskDTO;
import com.StackDash.Entity.Task;
import com.StackDash.Entity.User;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.List;
@Mapper(componentModel = "spring")
public interface TaskMapper {

    GetTaskDTO toDto(Task entity);

    List<GetTaskDTO> toDtoList(List<Task> entities);

    default Long map(User user) {
        return user != null ? user.getUserId() : null;
    }
}
