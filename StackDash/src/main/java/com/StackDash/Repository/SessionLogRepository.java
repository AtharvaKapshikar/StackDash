package com.StackDash.Repository;

import com.StackDash.Entity.SessionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {
    List<SessionLog> findByUserId(Long userId);


}
