package com.StackDash.Service;

import com.StackDash.DTOs.GetTaskDTO;
import com.StackDash.DTOs.TaskDto;
import com.StackDash.Entity.Task;
import com.StackDash.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {

    //public boolean saveTask(TaskDto task);
    public boolean saveTask(TaskDto dto);

    public Map getAllTaskCount();

    //public List<TaskDto> getAllTask();
    public List<TaskDto> getTasksPaginated(int page, int size, String sortField, String sortDirection);
    public int getTotalTaskCount();

    public List<Task> getUserTask(Long userId);

    public boolean updateTask(TaskDto task);

    public GetTaskDTO getTask(Long id);

    public boolean deleteTaskById(Long id);


}
