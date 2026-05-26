package com.company.bugtracker1.sla.entity;

public enum PriorityLevel {
    P1(15, 240),
    P2(30, 480),
    P3(60, 4320),
    P4(120, 5760);

    private final int responseSlaMinutes;
    private final int resolutionSlaMinutes;

    PriorityLevel(int responseSlaMinutes, int resolutionSlaMinutes) {
        this.responseSlaMinutes = responseSlaMinutes;
        this.resolutionSlaMinutes = resolutionSlaMinutes;
    }

    public int getResponseSlaMinutes() {
        return responseSlaMinutes;
    }

    public int getResolutionSlaMinutes() {
        return resolutionSlaMinutes;
    }
}
