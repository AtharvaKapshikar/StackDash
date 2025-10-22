package com.StackDash.Controller;


import com.StackDash.DTOs.GetTaskDTO;
import com.StackDash.DTOs.PageResponse;
import com.StackDash.DTOs.TaskDto;
import com.StackDash.Entity.NotificationType;
import com.StackDash.Entity.Task;
import com.StackDash.Entity.User;
import com.StackDash.Exception.TaskNotFoundException;
import com.StackDash.Service.NotificationService;
import com.StackDash.Service.TaskService;
import com.StackDash.Service.UserService;
import com.StackDash.ServiceImpl.TaskServiceImpl;
import jakarta.websocket.server.PathParam;
import org.mapstruct.control.MappingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class TaskController {

    public static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/assign-task")
    public ResponseEntity<?> assignTask(@RequestBody TaskDto dto) {
        try {
            log.trace("Inside assignTask api: {}", dto);
            if (dto == null) {
                log.warn("Task is empty or null");
                return ResponseEntity.status(405).body("Task is empty, please fill details");
            }

            Optional<User> assignedByUser = userService.findByUserId(dto.getAssignedById());
            if (assignedByUser.isEmpty()) {
                log.warn("Assign By User is not found");
                return ResponseEntity.status(404).body("Assign By User is not present");
            }
            log.trace("Assign By Id is fetched: {}", assignedByUser);

            Optional<User> assignedToUser = userService.findByUserId(dto.getAssignedToId());
            if (assignedToUser.isEmpty()) {
                log.warn("Assign TO User is not found");
                return ResponseEntity.status(404).body("Assign To User is not present");
            }

            boolean status = taskService.saveTask(dto);
            if (!status) {
                log.warn("Task is not saved: {}", status);
                return ResponseEntity.status(500).body("Unable to save Task data");
            }
            log.info("Task is saved successfully : {}", status);
            log.info("Creating notification for userId={}, type={}, message={}",
                    dto.getAssignedToId(), NotificationType.TASK, "New task assigned to you!");
            notificationService.createNotification(dto.getAssignedToId(), NotificationType.TASK,
                    "New task assigned to you! check in task tab.");

            return ResponseEntity.ok("Task saved successfully");
        } catch (Exception ex) {
            log.error("Task is not saved error occured : {}", ex.getMessage());
            return ResponseEntity.status(503).body("Error occured while saving Task");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/task/count")
    public ResponseEntity<?> getAllTasksCount() {
        try {
            Map count = taskService.getAllTaskCount();
            log.info("Task count fetcehd : {}", count);
            return ResponseEntity.ok(count);
        } catch (Exception ex) {
            log.error("Error occured while fetching user task count: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occured while fetching user task count");
        }
    }

    @GetMapping("/admin/all-task")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "due_date") String sortField,
                                         @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            List<TaskDto> tasks = taskService.getTasksPaginated(page, size, sortField, sortDirection);
            int totalElements = taskService.getTotalTaskCount();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            log.info("tasks : {}", tasks);

            Map<String, Object> response = new HashMap<>();
            response.put("content", tasks);
            response.put("totalPages", totalPages);
            response.put("totalElements", totalElements);
            response.put("size", size);
            response.put("number", page);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("error {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occurred while fetching tasks");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update-task")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto dto) {
        try {
            if (dto == null) {
                log.warn("Task is empty or null");
                return ResponseEntity.status(405).body("Task is empty, please fill details");
            }

            Optional<User> assignedByUser = userService.findByUserId(dto.getAssignedById());
            if (assignedByUser.isEmpty()) {
                log.warn("Assign By User is not found");
                return ResponseEntity.status(404).body("Assign by user not found! please enter valid User Id");
            }
            log.trace("Assign By Id is fetched: {}", assignedByUser);

            Optional<User> assignedToUser = userService.findByUserId(dto.getAssignedToId());
            if (assignedToUser.isEmpty()) {
                log.warn("Assign TO User is not found");
                return ResponseEntity.status(404).body("Assign to user not found! please enter valid User Id");
            }

            boolean status = taskService.updateTask(dto);
            if (!status) {
                log.warn("Task is not updated: {}", status);
                return ResponseEntity.status(500).body("Unable to update Task data");
            }
            log.info("Task is updated successfully : {}", status);

            notificationService.createNotification(dto.getAssignedToId(), NotificationType.UPDATETASK,
                    "Your Task is updated! check in task tab.");

            return ResponseEntity.ok("Task updated successfully");
        } catch (Exception ex) {
            log.error("Task is not updated error occurred : {}", ex.getMessage());
            return ResponseEntity.status(503).body("Error occurred while updating Task");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/admin/task/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) {
        try {
            GetTaskDTO task = taskService.getTask(id);
            log.info("Task id : {}", id);
            if (task == null) {
                throw new TaskNotFoundException("Task not found. Please check task id");
                //return ResponseEntity.status(404).body("Task not found. Please check task id");
            }
            log.info("Task fetched : {}", task);
            return ResponseEntity.ok(task);
        } catch (Exception ex) {
            log.error("Error occurred while fetching task: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occurred while fetching task");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/task/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        try {
            log.info("Inside delete api: {}", id);
            boolean isDeleted = taskService.deleteTaskById(id);
            log.info("deleted method ran : {}", isDeleted);
            if (!isDeleted) return ResponseEntity.ok("Unable to delete task!");
            return ResponseEntity.ok("Task deleted successfully!");
        } catch (Exception ex) {
            return ResponseEntity.status(501).body("Error Occurred while deleting task, please try later.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("user/tasks/{userId}")
    public ResponseEntity<?> geUserTasks(@PathVariable("userId") Long assignedToId){
        try{
            log.info("task id : {}", assignedToId);
            if(assignedToId <= 0){
                return ResponseEntity.status(500).body("Invalid user id");
            }

            List<Task> userTask = taskService.getUserTask(assignedToId);

            if(userTask.isEmpty()){
                return ResponseEntity.status(404).body("No task assign to you.");
            }

            return ResponseEntity.ok(userTask);
        }catch (Exception ex){
            return ResponseEntity.status(503).body("Unexpected error occurred, please try later");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/user/update-task")
    public ResponseEntity<?> updateUserTask(@RequestBody TaskDto dto) {
        try {
            if (dto == null) {
                log.warn("Task is empty or null");
                return ResponseEntity.status(405).body("Task is empty, please fill details");
            }

            Optional<User> assignedByUser = userService.findByUserId(dto.getAssignedById());
            if (assignedByUser.isEmpty()) {
                log.warn("Assign By User is not found");
                return ResponseEntity.status(404).body("Assign by user not found! please enter valid User Id");
            }
            log.trace("Assign By Id is fetched: {}", assignedByUser);

            Optional<User> assignedToUser = userService.findByUserId(dto.getAssignedToId());
            if (assignedToUser.isEmpty()) {
                log.warn("Assign TO User is not found");
                return ResponseEntity.status(404).body("Assign to user not found! please enter valid User Id");
            }

            boolean status = taskService.updateTask(dto);
            if (!status) {
                log.warn("Task is not updated: {}", status);
                return ResponseEntity.status(500).body("Unable to update Task data");
            }
            log.info("Task is updated successfully : {}", status);

            notificationService.createNotification(dto.getAssignedToId(), NotificationType.UPDATETASK,
                    "Your Task is updated! check in task tab.");

            return ResponseEntity.ok("Task updated successfully");
        } catch (Exception ex) {
            log.error("Task is not updated error occurred : {}", ex.getMessage());
            return ResponseEntity.status(503).body("Error occurred while updating Task");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/user/task/{id}")
    public ResponseEntity<?> getUserTask(@PathVariable("id") Long id) {
        try {
            GetTaskDTO task = taskService.getTask(id);
            log.info("Task id : {}", id);
            if (task == null) {
                throw new TaskNotFoundException("Task not found. Please check task id");
                //return ResponseEntity.status(404).body("Task not found. Please check task id");
            }
            log.info("Task fetched : {}", task);
            return ResponseEntity.ok(task);
        } catch (Exception ex) {
            log.error("Error occurred while fetching task: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occurred while fetching task");
        }
    }
}
