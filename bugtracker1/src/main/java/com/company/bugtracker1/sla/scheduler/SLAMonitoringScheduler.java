package com.company.bugtracker1.sla.scheduler;

import com.company.bugtracker1.sla.entity.SLATracking;
import com.company.bugtracker1.sla.repository.SLATrackingRepository;
import com.company.bugtracker1.sla.service.SLATrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SLAMonitoringScheduler {

    private final SLATrackingRepository slaTrackingRepository;
    private final SLATrackingService slaTrackingService;

    @Scheduled(fixedDelay = 60000, initialDelay = 10000)
    public void monitorSLABreaches() {
        try {
            log.debug("Starting SLA breach monitoring...");
            
            slaTrackingService.updateRemainingTime();
            
            List<SLATracking> potentialBreaches = slaTrackingRepository.findPotentialBreaches();
            
            log.info("Found {} potential SLA breaches", potentialBreaches.size());
            
            for (SLATracking slaTracking : potentialBreaches) {
                log.warn("SLA Breach detected - Ticket ID: {}, SLA Type: {}", 
                        slaTracking.getTicket().getId(), slaTracking.getSlaType());
            }
        } catch (Exception e) {
            log.error("Error during SLA breach monitoring", e);
        }
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 30000)
    public void cleanupCompletedSLAs() {
        try {
            log.debug("Running cleanup for completed SLAs...");
            log.info("Cleanup completed");
        } catch (Exception e) {
            log.error("Error during SLA cleanup", e);
        }
    }
}
