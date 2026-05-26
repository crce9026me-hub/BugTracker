package com.company.bugtracker1.sla.repository;

import com.company.bugtracker1.sla.entity.SLAPolicy;
import com.company.bugtracker1.sla.entity.SLAType;
import com.company.bugtracker1.sla.entity.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SLAPolicyRepository extends JpaRepository<SLAPolicy, Long> {

    Optional<SLAPolicy> findByProjectIdAndPriorityLevelAndSlaType(Long projectId, PriorityLevel priorityLevel, SLAType slaType);

    List<SLAPolicy> findByProjectId(Long projectId);

    List<SLAPolicy> findByProjectIdAndIsActiveTrue(Long projectId);

    List<SLAPolicy> findByProjectIdAndSlaType(Long projectId, SLAType slaType);

    @Query("SELECT sp FROM SLAPolicy sp WHERE sp.project.id = :projectId AND sp.priorityLevel = :priorityLevel AND sp.isActive = true")
    Optional<SLAPolicy> findActivePoliciesByProjectAndPriority(@Param("projectId") Long projectId, @Param("priorityLevel") PriorityLevel priorityLevel);

    @Query("SELECT sp FROM SLAPolicy sp WHERE sp.project.id = :projectId AND sp.slaType = :slaType AND sp.isActive = true")
    List<SLAPolicy> findActiveSLAPoliciesByProjectAndType(@Param("projectId") Long projectId, @Param("slaType") SLAType slaType);
}
