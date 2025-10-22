package com.StackDash.Controller;

import com.StackDash.Configuration.JwtUtills;
import com.StackDash.DTOs.*;
import com.StackDash.Entity.NotificationType;
import com.StackDash.Entity.Role;
import com.StackDash.Entity.SessionLog;
import com.StackDash.Entity.User;
import com.StackDash.Repository.RoleRepository;
import com.StackDash.Repository.SessionLogRepository;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/user/")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private GmailService gmailService;

    @Autowired
    SessionLogRepository sessionLogRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtills jwtUtills;

    @Autowired(required=true)
    private AuthenticationManager authenticationManager;



    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        try{
            log.info("id from getUSer controller method: {}",id);
            if(id == null) {
                log.warn("Id is empty");
                return ResponseEntity.status(400).body("Id is Empty, please enter id");
            }

            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userService.getUserByUserName(currentUsername);

            if (!currentUser.getUserId().equals(id)) {
                return ResponseEntity.status(403).body("Access denied");
            }

            User user = userService.getUser(id);
            if(user == null){
                log.warn("User not found : {}", id);
                return ResponseEntity.status(404).body("User not found, please register");
            }
            log.info("User found : { }");

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
            log.error("User not found please enter valid details" + ex.getMessage());
            return ResponseEntity.status(400).body("User not found please enter valid details");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserDTO user) {
        try {
            log.info("Update request received for user: {}", user);

            if (user == null) {
                log.warn("User details are empty");
                return ResponseEntity.status(400).body("User details are empty, please fill in the details");
            }

            notificationService.createNotification(user.getUserId() , NotificationType.UPDATEUSER,
                    "Hi Dear, Your details are updated.");
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

}




