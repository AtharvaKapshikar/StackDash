package com.StackDash.Repository;

import com.StackDash.DTOs.TaskDto;
import com.StackDash.Entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    public List<Task> findByStatus(String status);

    public List<Task> findByAssignedToId_UserId(Long userId);

    @Query(value = """
        SELECT * FROM (
            SELECT a.*, ROWNUM rnum FROM (
                SELECT * FROM stack_dash_task ORDER BY due_date DESC
            ) a
            WHERE ROWNUM <= :endRow
        )
        WHERE rnum > :startRow
        """, nativeQuery = true)
    List<Task> findPaginated(@Param("startRow") int startRow, @Param("endRow") int endRow);

    @Query(value = """
    SELECT id, title, description, due_date, status, assigned_by_id, assigned_to_id
    FROM (
        SELECT a.*, ROWNUM rnum FROM (
            SELECT * FROM stack_dash_task ORDER BY due_date DESC
        ) a
        WHERE ROWNUM <= :endRow
    )
    WHERE rnum > :startRow
    """, nativeQuery = true)
    List<Object[]> findPaginatedRaw(@Param("startRow") int startRow, @Param("endRow") int endRow);

    @Query(value = "SELECT COUNT(*) FROM stack_dash_task", nativeQuery = true)
    int countAllTasks();


}
