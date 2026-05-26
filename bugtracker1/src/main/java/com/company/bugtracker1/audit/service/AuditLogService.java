package com.company.bugtracker1.audit.service;

import com.company.bugtracker1.audit.entity.AuditLog;
import com.company.bugtracker1.audit.repository.AuditLogRepository;
import com.company.bugtracker1.ticket.entity.Ticket;
import com.company.bugtracker1.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(Ticket ticket, String action, String description, String entityType, 
                         Long entityId, String oldValue, String newValue) {
        User performedBy = getCurrentUser();

        AuditLog auditLog = AuditLog.builder()
                .ticket(ticket)
                .action(action)
                .description(description)
                .performedBy(performedBy)
                .entityType(entityType)
                .entityId(entityId)
                .oldValue(oldValue)
                .newValue(newValue)
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
        log.info("Audit log created - Action: {}, Ticket ID: {}", action, ticket.getId());
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getTicketAuditLogs(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getProjectAuditLogs(Long projectId) {
        return auditLogRepository.findByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByActionContaining(action);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogRepository.findByCreatedAtBetween(startTime, endTime);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
