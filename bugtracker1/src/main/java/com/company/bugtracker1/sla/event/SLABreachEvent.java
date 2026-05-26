package com.company.bugtracker1.sla.event;

import com.company.bugtracker1.sla.entity.SLABreach;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SLABreachEvent extends ApplicationEvent {
    private final SLABreach breach;

    public SLABreachEvent(Object source, SLABreach breach) {
        super(source);
        this.breach = breach;
    }
}
