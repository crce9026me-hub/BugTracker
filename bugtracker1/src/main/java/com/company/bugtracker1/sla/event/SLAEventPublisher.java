package com.company.bugtracker1.sla.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SLAEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;

    public void publishSLABreachEvent(Object source, com.company.bugtracker1.sla.entity.SLABreach breach) {
        eventPublisher.publishEvent(new SLABreachEvent(source, breach));
        log.info("SLA breach event published for ticket: {}", breach.getTicket().getId());
    }
}
