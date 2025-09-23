package com.example.AppUsageTracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUsageRepo extends JpaRepository<AppUsage,Long> {

    @Query("""
            select a from AppUsage a
            where a.appName = :appName and a.usageDate = :usageDate
            """)
    AppUsage findByApp_UsageDate(@Param("appName")String currentAppName, @Param("usageDate") Date currentDate);

    List<AppUsage> findByUsageDate(Date currentDate);
}
