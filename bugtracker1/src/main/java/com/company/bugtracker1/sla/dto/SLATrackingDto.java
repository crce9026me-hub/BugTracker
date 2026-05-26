package com.company.bugtracker1.sla.dto;

import com.company.bugtracker1.sla.entity.SLAType;
import com.company.bugtracker1.sla.entity.SLAStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SLATrackingDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SLATrackingResponse {
        private Long id;
        private Long ticketId;
        private SLAType slaType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long remainingMinutes;
        private SLAStatus status;
        private Boolean isBreached;
        private LocalDateTime breachTimestamp;
        private LocalDateTime pausedAt;
        private Long totalPausedDurationMinutes;
        private String healthStatus;
        private Double percentageComplete;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StartSLARequest {
        @JsonProperty("ticket_id")
        private Long ticketId;

        @JsonProperty("sla_type")
        private SLAType slaType;

        @JsonProperty("priority_level")
        private String priorityLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StopSLARequest {
        @JsonProperty("ticket_id")
        private Long ticketId;

        @JsonProperty("sla_type")
        private SLAType slaType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PauseSLARequest {
        @JsonProperty("ticket_id")
        private Long ticketId;

        @JsonProperty("sla_type")
        private SLAType slaType;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResumeSLARequest {
        @JsonProperty("ticket_id")
        private Long ticketId;

        @JsonProperty("sla_type")
        private SLAType slaType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SLAMetrics {
        private Integer totalSLAs;
        private Integer activeSLAs;
        private Integer completedSLAs;
        private Integer breachedSLAs;
        private Double breachPercentage;
        private Long averageResponseTime;
        private Long averageResolutionTime;
    }
}
