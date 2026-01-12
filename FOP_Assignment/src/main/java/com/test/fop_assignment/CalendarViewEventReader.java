/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.fop_assignment;

/**
 *
 * @author User
 */

import java.io.*;
import java.time.LocalDateTime;


public class CalendarViewEventReader{

public static String[][] readEvents(int year, int month) {
    String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    File fileE = new File(folderPath + "event.csv");

    
    String[][] eventDays = new String[32][3];
    for (int i = 0; i < 32; i++) {
        eventDays[i][0] = "0"; // Flag: "1" if events exist
        eventDays[i][1] = "";  // Combined Titles
        eventDays[i][2] = "";  // Combined Start Times
    }

    try {
        // Read One-time Events ---
        if (fileE.exists()) {
            try (BufferedReader brE = new BufferedReader(new FileReader(fileE))) {
                String line;
                brE.readLine(); // Skip header
                while ((line = brE.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    

                    if (parts.length < 4) continue;
                    LocalDateTime start = LocalDateTime.parse(parts[3].trim());

                    if (start.getYear() == year && start.getMonthValue() == month) {
                        int day = start.getDayOfMonth();
                        if (eventDays[day][0].equals("0")) {
                            eventDays[day][0] = "1";
                            eventDays[day][1] = parts[1].trim();
                            eventDays[day][2] = parts[3].trim();
                        } else {
                            // Append if multiple events exist on this day
                            eventDays[day][1] += " | " + parts[1].trim();
                            eventDays[day][2] += " | " + parts[3].trim();
                        }
                    }
                }
            }
        }

        //  Read Recurring Events ---
        RecurringEventsGenerator gen = new RecurringEventsGenerator();
        Object[][] recurring = gen.generateRecurringEvents();
        int total = gen.getCount();

        for (int i = 0; i < total; i++) {
            
            LocalDateTime start = LocalDateTime.parse(String.valueOf(recurring[i][2]));
            if (start.getYear() == year && start.getMonthValue() == month) {
                int day = start.getDayOfMonth();
                if (eventDays[day][0].equals("0")) {
                    eventDays[day][0] = "1";
                    eventDays[day][1] = String.valueOf(recurring[i][0]);
                    eventDays[day][2] = String.valueOf(recurring[i][2]);
                } else {
                    eventDays[day][1] += " | " + String.valueOf(recurring[i][0]);
                    eventDays[day][2] += " | " + String.valueOf(recurring[i][2]);
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Error processing events: " + e.getMessage());
    }
    return eventDays;
}
}
