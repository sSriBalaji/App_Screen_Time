package org.example.data_visualizer;

import java.util.Date;

public class DataModel {

    private long id;
    private Date usageDate;
    private String appName;
    private String appDetails;
    private long totalTime;;

    public DataModel() {
    }

    public DataModel(long id, Date usageDate, String appName, String appDetails, long totalTime) {
        this.id = id;
        this.usageDate = usageDate;
        this.appName = appName;
        this.appDetails = appDetails;
        this.totalTime = totalTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }

    public String getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(String appDetails) {
        this.appDetails = appDetails;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
