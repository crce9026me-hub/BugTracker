package com.company.bugtracker1.audit.repository;

import com.company.bugtracker1.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTicketId(Long ticketId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT al FROM AuditLog al WHERE al.ticket.project.id = :projectId ORDER BY al.createdAt DESC")
    List<AuditLog> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT al FROM AuditLog al WHERE al.ticket.id = :ticketId ORDER BY al.createdAt DESC")
    List<AuditLog> findByTicketIdOrderByCreatedAtDesc(@Param("ticketId") Long ticketId);

    @Query("SELECT al FROM AuditLog al WHERE al.action LIKE %:action% ORDER BY al.createdAt DESC")
    List<AuditLog> findByActionContaining(@Param("action") String action);
}
