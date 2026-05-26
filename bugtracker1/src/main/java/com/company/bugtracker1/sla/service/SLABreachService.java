package com.company.bugtracker1.sla.service;

import com.company.bugtracker1.exception.ResourceNotFoundException;
import com.company.bugtracker1.sla.entity.SLABreach;
import com.company.bugtracker1.sla.dto.SLABreachDto;
import com.company.bugtracker1.sla.repository.SLABreachRepository;
import com.company.bugtracker1.sla.mapper.SLABreachMapper;
import com.company.bugtracker1.user.entity.User;
import com.company.bugtracker1.user.repository.UserRepository;
import com.company.bugtracker1.audit.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SLABreachService {

    private final SLABreachRepository slaBreachRepository;
    private final SLABreachMapper slaBreachMapper;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public SLABreachDto.SLABreachResponse getBreachById(Long id) {
        SLABreach slaBreache = slaBreachRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SLA breach not found with id: " + id));

        return slaBreachMapper.toResponse(slaBreache);
    }

    @Transactional(readOnly = true)
    public List<SLABreachDto.SLABreachResponse> getBreachesByTicketId(Long ticketId) {
        return slaBreachRepository.findByTicketId(ticketId)
                .stream()
                .map(slaBreachMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SLABreachDto.SLABreachResponse> getUnacknowledgedBreaches() {
        return slaBreachRepository.findByIsAcknowledgedFalse()
                .stream()
                .map(slaBreachMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SLABreachDto.SLABreachResponse> getBreachesByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return slaBreachRepository.findByBreachTimestampBetween(startTime, endTime)
                .stream()
                .map(slaBreachMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SLABreachDto.SLABreachResponse acknowledgeBreache(Long breachId, SLABreachDto.AcknowledgeBreachRequest request) {
        SLABreach slaBreache = slaBreachRepository.findById(breachId)
                .orElseThrow(() -> new ResourceNotFoundException("SLA breach not found with id: " + breachId));

        User currentUser = getCurrentUser();
        slaBreache.setIsAcknowledged(true);
        slaBreache.setAcknowledgedBy(currentUser);
        slaBreache.setAcknowledgedAt(LocalDateTime.now());
        slaBreache.setAcknowledgmentNotes(request.getAcknowledgmentNotes());

        SLABreach updatedBreach = slaBreachRepository.save(slaBreache);
        
        auditLogService.logAction(slaBreache.getTicket(), "SLA_BREACH_ACKNOWLEDGED", 
                "SLA breach acknowledged: " + request.getAcknowledgmentNotes(), 
                "SLA_BREACH", slaBreache.getId(), "false", "true");

        log.info("SLA breach acknowledged with id: {}", breachId);

        return slaBreachMapper.toResponse(updatedBreach);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }
}
