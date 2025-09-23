package com.example.AppUsageTracker;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date usageDate;
    private String appName;
    private String appDetails;
    private long totalTime;

    public AppUsage(Date usageDate, String appName, String appDetails, long totalTime) {
        this.usageDate = usageDate;
        this.appName = appName;
        this.appDetails = appDetails;
        this.totalTime = totalTime;
    }
}
