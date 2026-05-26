package com.company.bugtracker1.sla.mapper;

import com.company.bugtracker1.sla.entity.SLABreach;
import com.company.bugtracker1.sla.dto.SLABreachDto;
import org.springframework.stereotype.Component;

@Component
public class SLABreachMapper {

    public SLABreachDto.SLABreachResponse toResponse(SLABreach slaBreache) {
        if (slaBreache == null) {
            return null;
        }

        Long acknowledgedById = null;
        if (slaBreache.getAcknowledgedBy() != null) {
            acknowledgedById = slaBreache.getAcknowledgedBy().getId();
        }

        return SLABreachDto.SLABreachResponse.builder()
                .id(slaBreache.getId())
                .ticketId(slaBreache.getTicket().getId())
                .slaType(slaBreache.getSlaType())
                .breachTimestamp(slaBreache.getBreachTimestamp())
                .expectedTime(slaBreache.getExpectedTime())
                .breachMinutes(slaBreache.getBreachMinutes())
                .isAcknowledged(slaBreache.getIsAcknowledged())
                .acknowledgedById(acknowledgedById)
                .acknowledgedAt(slaBreache.getAcknowledgedAt())
                .acknowledgmentNotes(slaBreache.getAcknowledgmentNotes())
                .createdAt(slaBreache.getCreatedAt())
                .build();
    }
}
