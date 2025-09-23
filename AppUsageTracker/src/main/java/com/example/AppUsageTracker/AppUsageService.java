package com.example.AppUsageTracker;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class AppUsageService {


    @Autowired
    private AppUsageRepo appUsageRepo;

    @Autowired
    private EntityManager entityManager;

    private Date truncateToDay(Instant instant) {
        // Convert Instant to LocalDate at the system default timezone and set to start of day
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        HWND GetForegroundWindow();
        int GetWindowThreadProcessId(HWND hWnd, int[] lpdwProcessId);
        int GetWindowText(HWND hwnd, char[] buffer, int length);
    }


    Instant start = Instant.now();
    Instant periodic = Instant.now();
    String currentAppName = "";
    String currentDetails = "";
    long currentPId = -1;

    @PostConstruct
    public void StartTracking(){
        new Thread(() ->{
            while(true){
                try{
                    int[] pIdArr = new int[1];
                    char[] buffer = new char[1024];

                    HWND hwnd = User32.INSTANCE.GetForegroundWindow();

                    if (hwnd == null) {
                        System.err.println("Foreground window null at " + Instant.now());
                        Thread.sleep(1000);
                        continue;
                    }

                    User32.INSTANCE.GetWindowText(hwnd,buffer,buffer.length);
                    User32.INSTANCE.GetWindowThreadProcessId(hwnd,pIdArr);

                    long pId = pIdArr[0];
                    String details = Native.toString(buffer);
                    Optional<ProcessHandle> ph = ProcessHandle.of(pId);
                    String cmd = ph.map(ProcessHandle::info)
                            .flatMap(ProcessHandle.Info::command)
                            .orElse("UnKnown");

                    String appName =(!cmd.equals("Unknown")) ? cmd.contains("\\") || cmd.contains("/") ?
                            cmd.substring(cmd.lastIndexOf("\\")+1).toLowerCase() :cmd.toLowerCase()
                            :
                            (details.substring(details.lastIndexOf("-")+1));


                    if(pId != currentPId || Duration.between(periodic,Instant.now()).getSeconds() >= 30){
                        Instant now = Instant.now();
                        periodic = Instant.now();

                        if(currentPId != -1){
                            long seconds = Duration.between(start,now).getSeconds();
                            Date currentDate = truncateToDay(now);
                            SaveUsageInTransaction(currentAppName,currentDetails,currentDate,seconds);
//                            AppUsage session = appUsageRepo.findByApp_UsageDate(currentAppName,currentDate);
//                            if(session == null){
//                                session = new AppUsage();
//                                session.setAppName(currentAppName);
//                                session.setAppDetails(currentDetails);
//                                session.setUsageDate(currentDate);
//                                session.setTotalTime(seconds);
//                            }
//                            else{
//                                long totalTime = session.getTotalTime();
//                                session.setTotalTime(seconds + totalTime);
//                            }
//                            appUsageRepo.save(session);
                        }

                        currentPId = pId;
                        currentDetails = details;
                        currentAppName = appName;
                        start = now;


                    }


                    Thread.sleep(1000);

                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }catch (Exception e){
                    System.err.println("Error in tracking: " + e.getMessage());
                }

            }
        }).start();
    }

    @Transactional
    public void SaveUsageInTransaction(String currentAppName,String currentDetails,Date currentDate,long seconds){

        AppUsage session = appUsageRepo.findByApp_UsageDate(currentAppName,currentDate);
        if(session == null){
            session = new AppUsage();
            session.setAppName(currentAppName);
            session.setAppDetails(currentDetails);
            session.setUsageDate(currentDate);
            session.setTotalTime(seconds);
        }
        else{
            long totalTime = session.getTotalTime();
            session.setTotalTime(seconds + totalTime);
        }
        appUsageRepo.saveAndFlush(session);
    }
}
