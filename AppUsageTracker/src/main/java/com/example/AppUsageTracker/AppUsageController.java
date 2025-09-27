package com.example.AppUsageTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/app/screen")
public class AppUsageController {

    @Autowired
    private AppUsageLogicService appUsageLogicService;

    @GetMapping("/all")
    public List<AppUsage> GetScreenTime(){
        return appUsageLogicService.AllScreenTime();
    }

    @GetMapping("/today")
    public List<AppUsage> GetTodayScreenTime(){
        return appUsageLogicService.TodayScreenTime();
    }

    @GetMapping("/daily")
    public List<AppUsageDto> GetSumOfScreenTime(){
        return appUsageLogicService.dailySum();
    }

    @GetMapping("/date")
    public List<AppUsage> GetBasedOnDate(
            @RequestParam(value = "day") int day,
            @RequestParam(value = "month") int month,
            @RequestParam(value = "year") int year
    ){

        LocalDate date = LocalDate.of(year,month,day);
        return appUsageLogicService.getBasedOnDate(date);
    }

}
