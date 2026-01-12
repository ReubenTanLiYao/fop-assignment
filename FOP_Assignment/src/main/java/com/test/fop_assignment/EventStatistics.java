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
import java.time.*;
import java.util.*;

public class EventStatistics {

    static String event_file = System.getProperty("user.dir")+ File.separator + "data" + File.separator + "event.csv";

    public static void showStats() {

        int[] dayCount = new int[7];
        int[] monthCount = new int[12];

        int totalEvents = 0;
        long totalMinutes = 0;

        try {
            Scanner read = new Scanner(new File(event_file));

            // Skip header
            if (read.hasNextLine()) read.nextLine();

            while (read.hasNextLine()) {
                String line = read.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                LocalDateTime start = LocalDateTime.parse(parts[3].trim());
                LocalDateTime end   = LocalDateTime.parse(parts[4].trim());

               
                long minutes = Duration.between(start, end).toMinutes();
                totalMinutes += minutes;

                int day = start.getDayOfWeek().getValue() - 1;
                dayCount[day]++;

                int month = start.getMonthValue() - 1;
                monthCount[month]++;

                totalEvents++;
            }
            read.close();

        } catch (Exception e) {
            System.out.println("Error reading events file.");
            return;
        }

        String[] days = {
                "Monday","Tuesday","Wednesday",
                "Thursday","Friday","Saturday","Sunday"
        };

        String[] months = {
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        };

        int highestDayCount = 0;
        int highestMonthCount = 0;
        String busiestDay = "";
        String busiestMonth = "";

        System.out.println("\n\t\t   Event Statistics :");
        System.out.println("--------------------------------------------------------");

        if (totalEvents == 0) {
            System.out.println("No events found.");
            return;
        }

        long avgMinutes = totalMinutes / totalEvents;

        System.out.println("Total events: " + totalEvents);
        System.out.println("Total scheduled time: "
                + (totalMinutes / 60) + " hours "
                + (totalMinutes % 60) + " minutes");

        System.out.println("Average event duration: "
                + (avgMinutes / 60) + " hours "
                + (avgMinutes % 60) + " minutes");

        System.out.println("\nEvents per day of the week:");
        for (int i = 0; i < 7; i++) {
            System.out.println(days[i] + ": " + dayCount[i] + " event(s)");
            if (dayCount[i] > highestDayCount) {
                highestDayCount = dayCount[i];
                busiestDay = days[i];
            }
        }

        System.out.println("Busiest day: " + busiestDay
                + " (" + highestDayCount + " events)");

        System.out.println("\nEvents per month:");
        for (int i = 0; i < 12; i++) {
            System.out.println(months[i] + ": " + monthCount[i] + " event(s)");
            if (monthCount[i] > highestMonthCount) {
                highestMonthCount = monthCount[i];
                busiestMonth = months[i];
            }
        }

        System.out.println("Busiest month: " + busiestMonth
                + " (" + highestMonthCount + " events)");
    }
}
