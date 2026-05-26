package com.company.bugtracker1.sla.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class SLATimeCalculator {

    public static LocalDateTime calculateSLAEndTime(LocalDateTime startTime, int slaMinutes, 
                                                     boolean includeWeekends, boolean businessHoursOnly,
                                                     String businessHoursStart, String businessHoursEnd) {
        if (!businessHoursOnly) {
            return startTime.plusMinutes(slaMinutes);
        }

        return calculateBusinessHoursSLAEndTime(startTime, slaMinutes, includeWeekends, 
                                                businessHoursStart, businessHoursEnd);
    }

    private static LocalDateTime calculateBusinessHoursSLAEndTime(LocalDateTime startTime, int slaMinutes,
                                                                   boolean includeWeekends,
                                                                   String businessHoursStart, String businessHoursEnd) {
        LocalDateTime currentTime = startTime;
        long remainingMinutes = slaMinutes;

        while (remainingMinutes > 0) {
            if (!isBusinessHours(currentTime, includeWeekends, businessHoursStart, businessHoursEnd)) {
                currentTime = moveToNextBusinessHourStart(currentTime, businessHoursStart);
                continue;
            }

            LocalDateTime endOfBusinessDay = currentTime.withHour(Integer.parseInt(businessHoursEnd.split(":")[0]))
                    .withMinute(Integer.parseInt(businessHoursEnd.split(":")[1]))
                    .withSecond(0);

            long minutesUntilEndOfDay = getMinutesBetween(currentTime, endOfBusinessDay);

            if (minutesUntilEndOfDay >= remainingMinutes) {
                return currentTime.plusMinutes(remainingMinutes);
            } else {
                remainingMinutes -= minutesUntilEndOfDay;
                currentTime = moveToNextBusinessHourStart(endOfBusinessDay, businessHoursStart);
            }
        }

        return currentTime;
    }

    private static boolean isBusinessHours(LocalDateTime dateTime, boolean includeWeekends,
                                           String businessHoursStart, String businessHoursEnd) {
        if (!includeWeekends) {
            DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return false;
            }
        }

        LocalTime startTime = LocalTime.parse(businessHoursStart);
        LocalTime endTime = LocalTime.parse(businessHoursEnd);
        LocalTime currentTime = dateTime.toLocalTime();

        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }

    private static LocalDateTime moveToNextBusinessHourStart(LocalDateTime dateTime, String businessHoursStart) {
        LocalTime startTime = LocalTime.parse(businessHoursStart);
        LocalDateTime nextDay = dateTime.plusDays(1);
        return nextDay.withHour(startTime.getHour())
                .withMinute(startTime.getMinute())
                .withSecond(0);
    }

    private static long getMinutesBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MINUTES.between(start, end);
    }

    public static long getRemainingMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return java.time.temporal.ChronoUnit.MINUTES.between(LocalDateTime.now(), endTime);
    }

    public static boolean isBreached(LocalDateTime endTime) {
        return LocalDateTime.now().isAfter(endTime);
    }
}
