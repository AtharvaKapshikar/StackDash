package com.StackDash.ServiceImpl;

import com.StackDash.DTOs.GetTaskDTO;
import com.StackDash.DTOs.TaskDto;
import com.StackDash.Entity.Task;
import com.StackDash.Entity.User;
import com.StackDash.Exception.TaskNotFoundException;
import com.StackDash.Repository.TaskRepository;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.TaskService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    public static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public boolean saveTask(TaskDto dto) {
        try {
            log.info("Inside saveTask with DTO: {}", dto);

            Optional<User> assignedByUser = userRepository.findById(dto.getAssignedById());
            if (assignedByUser.isEmpty()) {
                log.warn("Assign By User is not present");
                return false;
            }
            log.trace("Assign By Id is fetched: {}", assignedByUser);

            Optional<User> assignedToUser = userRepository.findById(dto.getAssignedToId());
            if (assignedToUser.isEmpty()) {
                log.warn("Assign TO User is not present");
                return false;
            }
            log.trace("Assigned To Id is fetched: {}", assignedToUser);

            Task task = new Task();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            task.setDueDate(dto.getDueDate());
            task.setAssignedById(assignedByUser.get());
            task.setAssignedToId(assignedToUser.get());
            log.trace("Saved DTO data in Task Object: {}", task);

            taskRepository.save(task);
            log.info("Task saved successfully");
            return true;
        } catch (Exception ex) {
            log.error("Unable to save task", ex);
            return false;
        }
    }

    @Override
    public Map getAllTaskCount() {
        log.trace("Inside getALlTaskCount method from Service: { }");
        Long count = taskRepository.count();
        log.info("Count fetched :{}", count);
        List<Task> tasks = taskRepository.findByStatus("Completed");
        Long completedCount = (long) tasks.size();

        Map alltasks = new HashMap<>();
        alltasks.put(count, completedCount);
        return alltasks;
    }

    //  @Override
//    public List<TaskDto> getAllTask() {
//
//        log.info("Inside Service fetching tasks");
//        List<Task> tasks = taskRepository.findAll();
//        log.info("Inside Service tasks fetched : {}", tasks);
//        return tasks.stream()
//                .map(task -> new TaskDto(task)) // assuming TaskDto has a constructor that accepts Task
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<Task> getTasksPaginated(int pageNumber, int pageSize) {
//        int startRow = pageNumber * pageSize;
//        int endRow = startRow + pageSize;
//        return taskRepository.findPaginated(startRow, endRow);
//    }

//    public List<Task> getTasksPaginated(int page, int size) {
//        int startRow = page * size;
//        int endRow = startRow + size;
//        return taskRepository.findPaginated(startRow, endRow);
//    }

    public List<TaskDto> getTasksPaginated(int page, int size, String sortField, String sortDirection) {
        int startRow = page * size;
        int endRow = startRow + size;

        String query = String.format("""
                SELECT id, title, description, due_date, status, assigned_by_id, assigned_to_id
                FROM (
                    SELECT a.*, ROWNUM rnum FROM (
                        SELECT * FROM stack_dash_task ORDER BY %s %s
                    ) a
                    WHERE ROWNUM <= :endRow
                )
                WHERE rnum > :startRow
                """, sortField, sortDirection);

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("startRow", startRow);
        nativeQuery.setParameter("endRow", endRow);

        List<Object[]> rawResults = nativeQuery.getResultList();
        // map to DTOs as before...
        return rawResults.stream().map(row -> {
            TaskDto dto = new TaskDto();
            dto.setId(row[0] != null ? ((Long) row[0]) : null); // cast directly to Long
            dto.setTitle((String) row[1]);
            dto.setDescription((String) row[2]);
            dto.setDueDate(row[3] != null ? ((Timestamp) row[3]).toLocalDateTime().toLocalDate() : null);
            dto.setStatus((String) row[4]);
            dto.setAssignedById(row[5] != null ? ((Long) row[5]) : null);
            dto.setAssignedToId(row[6] != null ? ((Long) row[6]) : null);
            return dto;
        }).collect(Collectors.toList());

    }

    public int getTotalTaskCount() {
        return taskRepository.countAllTasks();
    }

    @Override
    public List<Task> getUserTask(Long userId) {
        return taskRepository.findByAssignedToId_UserId(userId);
    }

    @Override
    public boolean updateTask(TaskDto dto) {
        try {
            log.info("Inside updateTask with DTO: {}", dto);

            Optional<User> assignedByUser = userRepository.findById(dto.getAssignedById());
            if(assignedByUser.isEmpty()){
                log.warn("Assign By User is not present");
                return false;
            }
            log.trace("Assign By Id is fetched: {}", assignedByUser);

            Optional<User> assignedToUser = userRepository.findById(dto.getAssignedToId());
            if(assignedToUser.isEmpty()){
                log.warn("Assign TO User is not present");
                return false;
            }
            log.trace("Assigned To Id is fetched: {}", assignedToUser);

            Task task = new Task();
            task.setId(dto.getId());
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            task.setDueDate(dto.getDueDate());
            task.setAssignedById(assignedByUser.get());
            task.setAssignedToId(assignedToUser.get());
            log.trace("updated DTO data in Task Object: {}", task);

            taskRepository.save(task);
            log.info("Task updated successfully");
            return true;
        } catch (Exception ex) {
            log.error("Unable to update task", ex);
            return false;
        }
    }

    @Override
    public GetTaskDTO getTask(Long id) {
       Optional<Task> taskOtp =  taskRepository.findById(id);
       if(taskOtp.isEmpty()){
            throw new TaskNotFoundException("Task not found, please enter correct id");
       }
        GetTaskDTO task = new GetTaskDTO();
        task.setId(taskOtp.get().getId());
        task.setTitle(taskOtp.get().getTitle());
        task.setDescription(taskOtp.get().getDescription());
        task.setStatus(taskOtp.get().getStatus());
        task.setDueDate(taskOtp.get().getDueDate());
        task.setAssignedById(taskOtp.get().getAssignedById().getUserId());
        task.setAssignedToId(taskOtp.get().getAssignedToId().getUserId());
        return task;
    }

    @Override
    public boolean deleteTaskById(Long id) {
        try {
            taskRepository.deleteById(id);
            return true;
        }catch (Exception ex){
            log.error("Error while deleting");
            return false;
        }
    }
}
