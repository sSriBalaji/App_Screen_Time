package org.example.data_visualizer;

import java.util.Date;

public class DailyDataModel {
    private Date usageDate;
    private long totalTime;

    public DailyDataModel(Date usageDate, long totalTime) {
        this.usageDate = usageDate;
        this.totalTime = totalTime;
    }

    public DailyDataModel() {
    }

    public Date getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
