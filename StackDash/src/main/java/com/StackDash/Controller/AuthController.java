package com.StackDash.Controller;

import com.StackDash.Configuration.JwtUtills;
import com.StackDash.DTOs.LoginDTO;
import com.StackDash.DTOs.LoginResponseDTO;
import com.StackDash.DTOs.OtpVerificationDto;
import com.StackDash.DTOs.UpdatePasswordDTO;
import com.StackDash.Entity.NotificationType;
import com.StackDash.Entity.Role;
import com.StackDash.Entity.SessionLog;
import com.StackDash.Entity.User;
import com.StackDash.Repository.RoleRepository;
import com.StackDash.Repository.SessionLogRepository;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart("user") User user,
                                          @RequestPart("profilePicture") MultipartFile file) throws IOException {

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            log.error("Email already registered, please login to your account");
            return ResponseEntity.status(409).body("Email already registered");
        }

        if (userRepository.userExists(user.getUserName()) == 1) {
            log.warn("Username not available please try another");
            return ResponseEntity.status(409).body("Username already taken, please another");
        }

        Set<Role> inputRoles = user.getRoles();
        Set<Role> resolvedRoles = new HashSet<>();

        for (Role role : inputRoles) {
            log.trace("Role checking in for loop: { }");
            Role existingRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName()));
            resolvedRoles.add(existingRole);
        }
        log.debug("User role setting in user and role db : { }");
        user.setRoles(resolvedRoles); // Match your entity field name
       // user.setPassword(user.getPassword()); // Encrypt password
        user.setEnabled(true); //  Odefault user status
        user.setActive(false); // set login state
        user.setVerified((false));

        String otp = otpService.generateAndSaveOtp(user.getEmail());
        log.debug("OTP generated successfully : {} ", user.getEmail());
        gmailService.sendOtpToUser(user,otp);
        log.info("Email sent successfully to user : {}", user.getFirstName());

        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();;
        String uploadDir = "uploads/profile-pics/";
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        user.setProfilePicture("/uploads/profile-pics/" + fileName);

        notificationService.createNotification(user.getUserId(), NotificationType.REGISTERED,
                "Welcome! Your account has been created.");

        User savedUser = userService.saveUser(user);
        log.info("User saved successfully : {}", user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto dto){
        log.trace("otp and email: {} {}", dto.getEmail(), dto.getOtp());
        System.out.println(dto.getEmail()+ dto.getOtp());
        ResponseEntity<?> isValid = otpService.validateOtp(dto.getEmail(), dto.getOtp());
        log.debug("OTP validated successfully for: {}", dto.getEmail());

        if(isValid.getStatusCode().is2xxSuccessful()){
            log.debug("OTP validated successfully : { }");
            User user = userService.findByEmail(dto.getEmail());
            log.debug("User fetched: {}", user);
            if (user == null) {
                log.warn("User not found for email: {}", dto.getEmail());
                return ResponseEntity.status(404).body("User not found");
            }
            user.setActive(true); //  Mark user as verified
            user.setVerified(true);
            userService.saveUser(user);

            notificationService.createNotification(user.getUserId(), NotificationType.VERIFIED,
                    "Excellent ! Your email id is verified successfully");

            log.info("User successfully fetched and OTP verified now user is active : {}", user);
            return ResponseEntity.ok("OTP verified successfully. User activated.");
        } else {
            log.error("Invalid or Expired OTP for: {}", dto.getEmail());
            return ResponseEntity.status(401).body("Invalid or expired OTP");
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO dto, HttpServletRequest httpRequest) {
        log.info("Login attempt for username: {}", dto.getUserName());


        try {
            if (dto.getUserName() == null || dto.getUserName().isBlank()) {
                log.warn("Username is empty");
                return ResponseEntity.status(400).body("Username is empty");
            }

            if (userRepository.userExists(dto.getUserName()) == 0) {
                log.warn("User not found: {}", dto.getUserName());
                return ResponseEntity.status(404).body("User not found. Please register!");
            }

            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                log.warn("Password is empty for user: {}", dto.getUserName());
                return ResponseEntity.status(400).body("Password is empty");
            }

            boolean isValid = userService.validateUserByUserName(dto.getUserName(), dto.getPassword());
            log.trace("User validation result for {}: {}", dto.getUserName(), isValid);

            if (!isValid) {
                log.warn("Invalid credentials for: {}", dto.getUserName());
                return ResponseEntity.status(401).body("Invalid credentials!");
            }

            User user = userService.getUserByUserName(dto.getUserName());

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                            .collect(Collectors.toList())
            );

            String token = jwtUtills.generateToken(userDetails);

            // Build response DTO
            LoginResponseDTO response = new LoginResponseDTO();
            response.setUserId(user.getUserId());
            response.setUserName(user.getUserName());
            response.setToken(token);
            response.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));

            // Log session details
            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            LocalDateTime loginTime = LocalDateTime.now();

            SessionLog slog = new SessionLog();
            slog.setUserId(user.getUserId());
            slog.setIpAddress(ipAddress);
            slog.setUserAgent(userAgent);
            slog.setLoginTime(loginTime);
            sessionLogRepository.save(slog);

            log.info("Session log saved for user: {}", user.getUserName());
            log.info("User login successful: {}", dto.getUserName());

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Login error for {}: {}", dto.getUserName(), ex.getMessage());
            return ResponseEntity.status(400).body("Login failed: " + ex.getMessage());
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginDTO dto, HttpServletRequest httpRequest) {
        log.debug("Admin login attempt for: {}", dto.getUserName());

        try {
            if (dto.getUserName() == null || dto.getUserName().isBlank()) {
                log.warn("Username is empty");
                return ResponseEntity.status(400).body("Username is empty");
            }

            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                log.warn("Password is empty for: {}", dto.getUserName());
                return ResponseEntity.status(400).body("Password is empty");
            }

            if (userRepository.userExists(dto.getUserName()) == 0) {
                log.warn("User not found: {}", dto.getUserName());
                return ResponseEntity.status(404).body("User not found. Please register!");
            }

            boolean isValid = userService.validateUserByUserName(dto.getUserName(), dto.getPassword());
            log.trace("User validation executed for: {}", dto.getUserName());

            if (!isValid) {
                log.warn("Invalid credentials for: {}", dto.getUserName());
                return ResponseEntity.status(401).body("Invalid credentials!");
            }

            User user = userService.getUserByUserName(dto.getUserName());

            boolean isAdmin = user.getRoles().stream()
                    .map(role -> role.getName().toString().toUpperCase())
                    .anyMatch(name -> name.equals("ADMIN"));

            if (!isAdmin) {
                log.warn("Access denied for non-admin user: {}", dto.getUserName());
                return ResponseEntity.status(403).body("Access Denied");
            }

            // Convert to UserDetails for JWT
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                            .collect(Collectors.toList())
            );

            String token = jwtUtills.generateToken(userDetails);

            // Build response DTO
            LoginResponseDTO response = new LoginResponseDTO();
            response.setUserId(user.getUserId());
            response.setUserName(user.getUserName());
            response.setToken(token);
            response.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));

            // Log session metadata
            String ipAddress = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");
            LocalDateTime loginTime = LocalDateTime.now();

            SessionLog slog = new SessionLog();
            slog.setUserId(user.getUserId());
            slog.setIpAddress(ipAddress);
            slog.setUserAgent(userAgent);
            slog.setLoginTime(loginTime);
            sessionLogRepository.save(slog);

            log.info("Admin login successful: {}", dto.getUserName());
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Admin login error for {}: {}", dto.getUserName(), ex.getMessage());
            return ResponseEntity.status(500).body("Login failed: " + ex.getMessage());
        }
    }

    @GetMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email){
        log.info("email id: {}", email);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            log.error("Account not found with this email, please register");
            return ResponseEntity.status(404).body("Account not found with this email, please register");
        }

        String otp = otpService.generateAndSaveOtp(email);
        log.debug("OTP generated successfully : {} ", email);
        gmailService.sendOtpToUser(user.get(),otp);
        log.info("Email sent successfully to user : {}", user.get().getFirstName());

        return ResponseEntity.ok("Email sent successfully, please verify");

    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){

        ResponseEntity<?> isValid = otpService.validateOtp(updatePasswordDTO.getEmail(), updatePasswordDTO.getOtp());
        //log.info("password: {}", updatePasswordDTO.getPassword());
        if(isValid.getStatusCode().is2xxSuccessful()){
            log.debug("OTP validated successfully : { }");

            User user = userService.findByEmail(updatePasswordDTO.getEmail());
            log.debug("User fetched: {}", user);

            if (user == null) {
                log.warn("User not found for email: {}", updatePasswordDTO.getEmail());
                return ResponseEntity.status(404).body("User not found");
            }

            if (updatePasswordDTO.getPassword() == null || updatePasswordDTO.getPassword().isBlank()) {
                return ResponseEntity.badRequest().body("New password cannot be null or empty");
            }
             user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
            userRepository.save(user);

            notificationService.createNotification(user.getUserId(), NotificationType.PASSWORD,
                    "Your password updated successfully!");

            log.info("Password updated successfully: {}", user);
            return ResponseEntity.ok("Your password updated successfully!");
        } else {
            log.error("Invalid or Expired OTP for: {}", updatePasswordDTO.getEmail());
            return ResponseEntity.status(401).body("Invalid or expired OTP");
        }

    }


}
