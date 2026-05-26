package com.company.bugtracker1.sla.dto;

import com.company.bugtracker1.sla.entity.SLAType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SLABreachDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SLABreachResponse {
        private Long id;

        @JsonProperty("ticket_id")
        private Long ticketId;

        @JsonProperty("sla_type")
        private SLAType slaType;

        @JsonProperty("breach_timestamp")
        private LocalDateTime breachTimestamp;

        @JsonProperty("expected_time")
        private LocalDateTime expectedTime;

        @JsonProperty("breach_minutes")
        private Long breachMinutes;

        @JsonProperty("is_acknowledged")
        private Boolean isAcknowledged;

        @JsonProperty("acknowledged_by_id")
        private Long acknowledgedById;

        @JsonProperty("acknowledged_at")
        private LocalDateTime acknowledgedAt;

        @JsonProperty("acknowledgment_notes")
        private String acknowledgmentNotes;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AcknowledgeBreachRequest {
        @JsonProperty("acknowledgment_notes")
        private String acknowledgmentNotes;
    }
}
