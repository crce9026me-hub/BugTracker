package com.company.bugtracker1.sla.mapper;

import com.company.bugtracker1.sla.entity.SLATracking;
import com.company.bugtracker1.sla.dto.SLATrackingDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class SLATrackingMapper {

    public SLATrackingDto.SLATrackingResponse toResponse(SLATracking slaTracking) {
        if (slaTracking == null) {
            return null;
        }

        String healthStatus = getHealthStatus(slaTracking);
        Double percentageComplete = calculatePercentageComplete(slaTracking);

        return SLATrackingDto.SLATrackingResponse.builder()
                .id(slaTracking.getId())
                .ticketId(slaTracking.getTicket().getId())
                .slaType(slaTracking.getSlaType())
                .startTime(slaTracking.getStartTime())
                .endTime(slaTracking.getEndTime())
                .remainingMinutes(slaTracking.getRemainingMinutes())
                .status(slaTracking.getStatus())
                .isBreached(slaTracking.getIsBreached())
                .breachTimestamp(slaTracking.getBreachTimestamp())
                .pausedAt(slaTracking.getPausedAt())
                .totalPausedDurationMinutes(slaTracking.getTotalPausedDurationMinutes())
                .healthStatus(healthStatus)
                .percentageComplete(percentageComplete)
                .build();
    }

    private String getHealthStatus(SLATracking slaTracking) {
        if (slaTracking.getIsBreached()) {
            return "BREACHED";
        }

        if (slaTracking.getRemainingMinutes() == null) {
            return "UNKNOWN";
        }

        if (slaTracking.getRemainingMinutes() <= 0) {
            return "BREACHED";
        }

        if (slaTracking.getRemainingMinutes() <= 15) {
            return "CRITICAL";
        }

        if (slaTracking.getRemainingMinutes() <= 30) {
            return "WARNING";
        }

        return "HEALTHY";
    }

    private Double calculatePercentageComplete(SLATracking slaTracking) {
        if (slaTracking.getStartTime() == null || slaTracking.getEndTime() == null) {
            return 0.0;
        }

        long totalMinutes = ChronoUnit.MINUTES.between(slaTracking.getStartTime(), slaTracking.getEndTime());
        if (totalMinutes <= 0) {
            return 0.0;
        }

        long elapsedMinutes = ChronoUnit.MINUTES.between(slaTracking.getStartTime(), LocalDateTime.now());
        return Math.min(100.0, (elapsedMinutes / (double) totalMinutes) * 100);
    }
}
