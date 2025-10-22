package com.StackDash.ServiceImpl;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.DTOs.UpdateUserDTO;
import com.StackDash.Entity.Role;
import com.StackDash.Entity.RoleName;
import com.StackDash.Entity.User;
import com.StackDash.Repository.RoleRepository;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.RoleService;
import com.StackDash.Service.UserService;
import com.StackDash.Utils.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User saveUser(User user) {
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        log.info("Into Save user method in serve class : { }");
       return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        log.trace("Into findByEmail method in Service class : {}", email);
        if (email == null || email.isEmpty()) {
            log.warn("Email is null or empty validated from service");
            throw new IllegalArgumentException("Email must not be empty");
        }
        log.info("Email validated : {}", email);
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (user.isEmpty()) {
            log.warn("User not found with email {}", email);
            throw new RuntimeException("User not found with email: " + email);
        }

        User user1 = user.get();
        log.info("Email validated and User found returning : {} ", email);
        return user1;
    }


    @Override
    public boolean validateUserByUserName(String username, String password) {
        boolean status = false;
        try {
            Optional<User> user = userRepository.findByUserName(username);
            if (user.isPresent()) {
                User user2 = user.get();
                if (user2.isVerified()) {
                    log.info("Fetched user: {}", user2.getUserName());
                    log.info("User ID: {}", user2.getUserId());
                    log.info("Password from DB: {}", user2.getPassword());
                    log.info("Raw password: {}", password);

                    boolean match = passwordEncoder.matches(password, user2.getPassword());
                    log.info("Match result: {}", match);

                    if (user2.getUserName().equals(username) && match) {
                        status = true;
                        user2.setActive(true);
                        user2.setLastLogin(LocalDateTime.now());
                        userRepository.save(user2);
                    }
                } else {
                    log.warn("User not verified: {}", username);
                }
            } else {
                log.warn("User not found: {}", username);
            }
        } catch (Exception ex) {
            log.error("Login error for {}: {}", username, ex.getMessage());
        }
        return status;
    }

    @Override
    public User getUser(Long id) {
        log.info("from getUser service class method {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found using id {}", id);
                    return new IllegalArgumentException("User not found using this id " + id);
                });
    }

    @Override
    public User getUserByUserName(String userName) {
        log.trace("Into findByUserName method in Service class : {}", userName);
        if (userName == null || userName.isEmpty()) {
            log.warn("Username is null or empty validated from service");
            throw new IllegalArgumentException("Username must not be empty");
        }
        log.info("Username validated : {}", userName);
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isEmpty()) {
            log.warn("Username not found with Username {}", userName);
            throw new RuntimeException("Username not found with Username: " + userName);
        }

        User user1 = user.get();
        log.info("Username validated and User found returning : {} ", userName);
        return user1;
    }

    @Override
    public Long getAllUsersCount() {
        log.info("Inside getUsers Method in Service: { }");
        Long count = (long) userRepository.findAll().size();
        log.info("Users count from service : {}", count);
        return count;
    }

    @Override
    public List<User> getActiveUsersCount() {
        return userRepository.findByActive(true);
    }

    public List<GetAllUserGTO> getAllUsers(int page, int size, String sortField, String sortDirection) {
        int startRow = page * size;
        int endRow = startRow + size;

        String direction = "ASC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";

        List<String> allowedFields = List.of(
                "user_id", "first_name", "last_name", "email",
                "mobile_number", "role", "designation"
        );
        if (!allowedFields.contains(sortField)) {
            sortField = "user_id";
        }

        String query = String.format("""
        SELECT u.user_id, u.first_name, u.last_name, u.email, u.mobile_number, u.designation,u.active, r.name
        FROM (
            SELECT a.*, ROWNUM rnum FROM (
                SELECT * FROM stack_dash_users ORDER BY %s %s
            ) a
            WHERE ROWNUM <= ?1
        ) u
        LEFT JOIN user_roles ur ON u.user_id = ur.user_id
        LEFT JOIN stack_dash_roles r ON ur.role_id = r.id
        WHERE rnum > ?2
        """, sortField, direction);

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, endRow);   // ?1 → endRow
        nativeQuery.setParameter(2, startRow); // ?2 → startRow


        @SuppressWarnings("unchecked")
        List<Object[]> rawResults = nativeQuery.getResultList();

        return rawResults.stream().map(row -> {
            GetAllUserGTO dto = new GetAllUserGTO();

            dto.setUserId(row[0] != null ? ((Long) row[0]) : null);
            dto.setFirstName((String) row[1]);
            dto.setLastName((String) row[2]);
            dto.setEmail((String) row[3]);
            dto.setMobileNumber((String) row[4]);
            dto.setDesignation((String) row[5]);
            dto.setActive((Integer) row[6]);

            // Convert string role names → RoleName enum → Role entity
            String rolesString = (String) row[7];
            if (rolesString != null && !rolesString.isBlank()) {
                Set<Role> roles = Arrays.stream(rolesString.split(","))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .map(roleNameStr -> {
                            try {
                                RoleName roleName = RoleName.valueOf(roleNameStr);
                                return roleRepository.findByName(roleName).orElse(null);
                            } catch (IllegalArgumentException e) {
                                return null; // skip invalid role names
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                dto.setRoles(roles);
            } else {
                dto.setRoles(Collections.emptySet());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByUserId(Long userID) {
        return userRepository.findById(userID);
    }

    @Override
    public boolean updateUser(UpdateUserDTO dto) {
        try {
            Optional<User> userOpt = userRepository.findById(dto.getUserId());
            if(userOpt.isEmpty()){
                throw new RuntimeException("User not Found");
            }
            User user = userOpt.get();

            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(dto.getLastName());
            }
            if (dto.getMobileNumber() != null) {
                user.setMobileNumber(dto.getMobileNumber());
            }
            if (dto.getCity() != null) {
                user.setCity(dto.getCity());
            }
            if (dto.getAge() != null) {
                user.setAge(dto.getAge());
            }
            if (dto.getDesignation() != null) {
                user.setDesignation(dto.getDesignation());
            }
            if (dto.isActive() == false || dto.isActive() == true) {
                user.setActive(dto.isActive());
            }
            if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
                Set<Role> updatedRoles = dto.getRoles().stream()
                        .map(roleDTO -> {
                            RoleName roleNameEnum = RoleName.valueOf(roleDTO.getName().toUpperCase());
                            return roleRepository.findByName(roleNameEnum)
                                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleDTO.getName()));
                        })
                        .collect(Collectors.toSet());

                user.setRoles(updatedRoles);
            }


            log.info("Added im user: {}", user);
            userRepository.save(user);
            return true;
        }catch (Exception ex){
            log.error("error : {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUserById(Long userId) {
        try{
            userRepository.deleteById(userId);
            log.info("user deleted: { }");
            return true;
        }catch (Exception ex){
            log.error("user not deleted: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<GetAllUserGTO> getlatestUsers() {
        long listSize = 0;
        List<User> userList = userRepository.findAll();
        if(userList.size() > 10){
            listSize = userList.size()- 5;
        }
        log.info("User list: { }", userList);
        List<GetAllUserGTO> latestuser = userList.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        log.info("latest User list: { }", latestuser);
            return latestuser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());
    }

}
