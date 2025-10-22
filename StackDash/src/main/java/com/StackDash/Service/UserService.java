package com.StackDash.Service;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.DTOs.TaskDto;
import com.StackDash.DTOs.UpdateUserDTO;
import com.StackDash.DTOs.UserPageResponse;
import com.StackDash.Entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User saveUser(User user);

    public User findByEmail(String email);

    public boolean validateUserByUserName(String username, String password);
//    public boolean validateUserByEmail(String email, String password);

    public User getUser(Long id);

    public User getUserByUserName(String userName);

    public Long getAllUsersCount();

    public List<User> getActiveUsersCount();

    public List<GetAllUserGTO> getAllUsers(int page, int size, String sortField, String sortDirection);

    public Optional<User> findByUserId(Long userID);

    public boolean updateUser(UpdateUserDTO user);

    public boolean deleteUserById(Long userId);

    public List<GetAllUserGTO> getlatestUsers();




}
