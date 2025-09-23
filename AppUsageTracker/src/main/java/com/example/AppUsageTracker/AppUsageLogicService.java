package com.example.AppUsageTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class AppUsageLogicService {

    @Autowired
    private AppUsageRepo appUsageRepo;

    private Date truncateToDay(Instant instant) {
        LocalDate localDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
    }

    public List<AppUsage> AllScreenTime() {
        return appUsageRepo.findAll();
    }

    public List<AppUsage> TodayScreenTime() {
        Date currentDate = truncateToDay(Instant.now());
        return appUsageRepo.findByUsageDate(currentDate);
    }
}
