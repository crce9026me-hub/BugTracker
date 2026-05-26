package com.company.bugtracker1.sla.mapper;

import com.company.bugtracker1.sla.entity.SLAPolicy;
import com.company.bugtracker1.sla.dto.SLAPolicyDto;
import org.springframework.stereotype.Component;

@Component
public class SLAPolicyMapper {

    public SLAPolicyDto.SLAPolicyResponse toResponse(SLAPolicy slaPolicy) {
        if (slaPolicy == null) {
            return null;
        }

        return SLAPolicyDto.SLAPolicyResponse.builder()
                .id(slaPolicy.getId())
                .projectId(slaPolicy.getProject().getId())
                .priorityLevel(slaPolicy.getPriorityLevel())
                .slaType(slaPolicy.getSlaType())
                .slaMinutes(slaPolicy.getSlaMinutes())
                .includeWeekends(slaPolicy.getIncludeWeekends())
                .includeBusinessHoursOnly(slaPolicy.getIncludeBusinessHoursOnly())
                .businessHoursStart(slaPolicy.getBusinessHoursStart())
                .businessHoursEnd(slaPolicy.getBusinessHoursEnd())
                .isActive(slaPolicy.getIsActive())
                .createdAt(slaPolicy.getCreatedAt())
                .updatedAt(slaPolicy.getUpdatedAt())
                .build();
    }
}
