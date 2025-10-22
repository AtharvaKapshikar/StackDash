package com.StackDash.Repository;

import com.StackDash.DTOs.GetAllUserGTO;
import com.StackDash.DTOs.TaskDto;
import com.StackDash.DTOs.UpdateUserDTO;
import com.StackDash.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT * FROM stack_dash_users WHERE email = :email AND ROWNUM = 1", nativeQuery = true)
    Optional<User> findFirstByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);



    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM stack_dash_users WHERE user_name = :userName) THEN 1 ELSE 0 END FROM dual", nativeQuery = true)
    int userExists(@Param("userName") String userName);

    public Optional<User> findByUserName(String userName);

    public List<User> findByActive(boolean value);

    //public List<GetAllUserGTO> findAll();

    public User findTaskByUserName(String userName);

    public User save(UpdateUserDTO userDTO);

//    @Query(value = """
//    SELECT * FROM (
//        SELECT u.*, ROWNUM rnum
//        FROM stack_dash_users u
//        WHERE ROWNUM <= :end
//    )
//    WHERE rnum > :start
//    """, nativeQuery = true)
//    List<User> findUsersWithPagination(@Param("start") int start, @Param("end") int end);

    @Query(value = "SELECT COUNT(*) FROM stack_dash_users", nativeQuery = true)
    Long countAllUsers();

}
