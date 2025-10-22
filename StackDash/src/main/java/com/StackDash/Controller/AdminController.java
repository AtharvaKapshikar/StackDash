package com.StackDash.Controller;

import com.StackDash.Configuration.JwtUtills;
import com.StackDash.DTOs.*;
import com.StackDash.Entity.NotificationType;
import com.StackDash.Entity.Role;
import com.StackDash.Entity.SessionLog;
import com.StackDash.Entity.User;
import com.StackDash.Repository.SessionLogRepository;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.NotificationService;
import com.StackDash.Service.RoleService;
import com.StackDash.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/")
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    SessionLogRepository sessionLogRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtills jwtUtills;

    @Autowired(required=true)
    private AuthenticationManager authenticationManager;



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try{
            log.info("id from getUSer controller method: {}",id);

            // Optional: log who is requesting the data
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Admin '{}' is requesting user ID: {}", currentUsername, id);

            User user = userService.getUser(id);
            log.info("User found : {}", user);

            DashboardUserDTO dto = new DashboardUserDTO();
            dto.setUserId(user.getUserId());
            dto.setUserName(user.getUserName());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setDesignation(user.getDesignation());
            dto.setProfilePicture(user.getProfilePicture());
            dto.setEmail(user.getEmail());
            dto.setCity(user.getCity());
            dto.setMobileNumber(user.getMobileNumber());
            dto.setAge(user.getAge());
            dto.setActive(user.isActive());
            dto.setVerified(user.isVerified());
            dto.setLastLogin(user.getLastLogin());
            List<String> roleNames = user.getRoles().stream()
                    .map(role -> role.getName().name()) // `.name()` converts enum to String
                    .collect(Collectors.toList());
            dto.setRoles(roleNames);

            log.info("User details : {} ", dto);
            return ResponseEntity.ok(dto);
        }catch (Exception ex ){
            log.error("Error while fetching user with ID {}: {}", id, ex.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred while fetching user details.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/user-count")
    public ResponseEntity<?> getDashboardUserCount() {
        try {
            log.info("Inside Get All user count Controller");

            Long totalCount = userService.getAllUsersCount();
            List<User> activeUsers = userService.getActiveUsersCount();

            log.info("Total users: {}", totalCount);
            log.info("Active users: {}", activeUsers.size());

            Map<String, Object> usersCount = new HashMap<>();
            usersCount.put("User Count", totalCount);
            usersCount.put("Active User", activeUsers.size());

            log.info("Final user count map: {}", usersCount);
            return ResponseEntity.ok(usersCount);

        } catch (Exception ex) {
            log.error("Error occurred while fetching user count: {}", ex.getMessage());
            return ResponseEntity.status(503).body("Error occurred while fetching user count: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "due_date") String sortField,
                                         @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            List<GetAllUserGTO> users = userService.getAllUsers(page, size, sortField, sortDirection);
            Long totalUsers = userService.getAllUsersCount();
            int totalPages = (int) Math.ceil((double) totalUsers / size);
            log.info("users : {}", users);

            Map<String, Object> response = new HashMap<>();
            response.put("content", users);
            response.put("totalPages", totalPages);
            response.put("totalElements", totalUsers);
            response.put("size", size);
            response.put("number", page);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("error {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occurred while fetching users");
        }
    }

    @PutMapping("/update/user")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserDTO user) {
        try {
            log.info("Update request received for user: {}", user);

            if (user == null) {
                log.warn("User details are empty");
                return ResponseEntity.status(400).body("User details are empty, please fill in the details");
            }

            notificationService.createNotification(user.getUserId(), NotificationType.UPDATEUSER,
                    "Hi Dear, Your details are updated by Admin.");
            log.info("User notification sent");

            boolean isUpdated = userService.updateUser(user);

            if (!isUpdated) {
                log.warn("Unable to update user data for ID: {}", user.getUserId());
                return ResponseEntity.status(500).body("Unable to update user data for ID: " + user.getUserId());
            }

            log.info("User updated successfully: {}", user.getUserId());
            return ResponseEntity.ok("Data updated successfully");

        } catch (Exception ex) {
            log.error("Error occurred while updating user: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error occurred while updating user: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.findByUserId(userId);

            if (user.isEmpty()) {
                log.warn("User not found for deletion: {}", userId);
                return ResponseEntity.status(404).body("User does not exist, please enter a valid user ID.");
            }

            boolean isDeleted = userService.deleteUserById(userId);

            if (!isDeleted) {
                log.error("Failed to delete user: {}", userId);
                return ResponseEntity.status(500).body("Unable to delete user, please try again later.");
            }

            log.info("User deleted successfully: {}", userId);
            return ResponseEntity.ok("User deleted successfully.");

        } catch (Exception ex) {
            log.error("Error occurred while deleting user {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(503).body("Service unavailable, please try again later.");
        }
    }

    @GetMapping("/latestusers")
    public ResponseEntity<?> getLatestUsers(){
        try{
            List<GetAllUserGTO> latestUsers = userService.getlatestUsers();
            if(latestUsers == null || latestUsers.isEmpty()){
                log.warn("User not found: { }");
                return ResponseEntity.status(404)
                        .body("No any user found.");
            }
            return ResponseEntity.ok(latestUsers);
        }catch (Exception ex){
            log.error("Unexpected error occurred, please try later.");
            return ResponseEntity.status(500).body("Unexpected error occurred, please try later.");
        }
    }
}
