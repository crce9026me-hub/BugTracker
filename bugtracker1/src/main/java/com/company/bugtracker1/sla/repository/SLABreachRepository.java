package com.company.bugtracker1.sla.repository;

import com.company.bugtracker1.sla.entity.SLABreach;
import com.company.bugtracker1.sla.entity.SLAType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SLABreachRepository extends JpaRepository<SLABreach, Long> {

    List<SLABreach> findByTicketId(Long ticketId);

    List<SLABreach> findByTicketIdAndSlaType(Long ticketId, SLAType slaType);

    List<SLABreach> findByIsAcknowledgedFalse();

    List<SLABreach> findByBreachTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT sb FROM SLABreach sb WHERE sb.ticket.project.id = :projectId")
    List<SLABreach> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT sb FROM SLABreach sb WHERE sb.ticket.project.id = :projectId AND sb.isAcknowledged = false")
    List<SLABreach> findUnacknowledgedBreachesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(sb) FROM SLABreach sb WHERE sb.ticket.project.id = :projectId")
    Integer countBreachesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(sb) FROM SLABreach sb WHERE sb.ticket.project.id = :projectId AND sb.isAcknowledged = false")
    Integer countUnacknowledgedBreachesByProjectId(@Param("projectId") Long projectId);
}
