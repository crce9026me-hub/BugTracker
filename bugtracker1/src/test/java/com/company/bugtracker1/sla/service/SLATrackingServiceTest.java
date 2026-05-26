package com.company.bugtracker1.sla.service;

import com.company.bugtracker1.exception.ResourceNotFoundException;
import com.company.bugtracker1.project.entity.Project;
import com.company.bugtracker1.sla.entity.*;
import com.company.bugtracker1.sla.repository.SLATrackingRepository;
import com.company.bugtracker1.sla.repository.SLAPolicyRepository;
import com.company.bugtracker1.sla.repository.SLABreachRepository;
import com.company.bugtracker1.sla.mapper.SLATrackingMapper;
import com.company.bugtracker1.ticket.entity.Ticket;
import com.company.bugtracker1.ticket.entity.Priority;
import com.company.bugtracker1.ticket.entity.TicketStatus;
import com.company.bugtracker1.ticket.entity.SupportLevel;
import com.company.bugtracker1.ticket.repository.TicketRepository;
import com.company.bugtracker1.audit.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SLA Tracking Service Tests")
class SLATrackingServiceTest {

    @Mock
    private SLATrackingRepository slaTrackingRepository;

    @Mock
    private SLAPolicyRepository slaPolicyRepository;

    @Mock
    private SLABreachRepository slaBreachRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SLATrackingMapper slaTrackingMapper;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private SLATrackingService slaTrackingService;

    private Project project;
    private Ticket ticket;
    private SLAPolicy slaPolicy;

    @BeforeEach
    void setUp() {
        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .build();

        ticket = Ticket.builder()
                .id(1L)
                .ticketId("TICKET-001")
                .project(project)
                .priority(Priority.HIGH)
                .currentStatus(TicketStatus.OPEN)
                .supportLevel(SupportLevel.L1)
                .build();

        slaPolicy = SLAPolicy.builder()
                .id(1L)
                .project(project)
                .priorityLevel(PriorityLevel.P2)
                .slaType(SLAType.RESPONSE)
                .slaMinutes(30)
                .includeWeekends(true)
                .includeBusinessHoursOnly(false)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should initialize SLAs for a ticket")
    void testInitializeSLAs() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(slaPolicyRepository.findByProjectIdAndIsActiveTrue(project.getId()))
                .thenReturn(java.util.List.of(slaPolicy));

        slaTrackingService.initializeSLAs(1L);

        verify(ticketRepository, times(1)).findById(1L);
        verify(slaPolicyRepository, times(1)).findByProjectIdAndIsActiveTrue(project.getId());
    }

    @Test
    @DisplayName("Should throw exception when ticket not found for SLA initialization")
    void testInitializeSLAsTicketNotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> slaTrackingService.initializeSLAs(999L));
    }

    @Test
    @DisplayName("Should start a new SLA")
    void testStartSLA() {
        SLATracking slaTracking = SLATracking.builder()
                .id(1L)
                .ticket(ticket)
                .slaType(SLAType.RESPONSE)
                .status(SLAStatus.ACTIVE)
                .isBreached(false)
                .build();

        when(slaTrackingRepository.findByTicketIdAndSlaType(1L, SLAType.RESPONSE))
                .thenReturn(Optional.empty());
        when(slaPolicyRepository.findByProjectIdAndPriorityLevelAndSlaType(
                project.getId(), PriorityLevel.P2, SLAType.RESPONSE))
                .thenReturn(Optional.of(slaPolicy));
        when(slaTrackingRepository.save(any(SLATracking.class))).thenReturn(slaTracking);

        slaTrackingService.startSLA(ticket, SLAType.RESPONSE);

        verify(slaTrackingRepository, times(1)).save(any(SLATracking.class));
        verify(auditLogService, times(1)).logAction(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should stop an active SLA")
    void testStopSLA() {
        SLATracking slaTracking = SLATracking.builder()
                .id(1L)
                .ticket(ticket)
                .slaType(SLAType.RESPONSE)
                .status(SLAStatus.ACTIVE)
                .isBreached(false)
                .build();

        when(slaTrackingRepository.findByTicketIdAndSlaType(1L, SLAType.RESPONSE))
                .thenReturn(Optional.of(slaTracking));
        when(slaTrackingRepository.save(any(SLATracking.class))).thenReturn(slaTracking);

        slaTrackingService.stopSLA(1L, SLAType.RESPONSE);

        verify(slaTrackingRepository, times(1)).save(any(SLATracking.class));
        assertEquals(SLAStatus.COMPLETED, slaTracking.getStatus());
    }
}
