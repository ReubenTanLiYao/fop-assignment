/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.fop_assignment;

/**
 *
 * @author User
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class BasicSearchEventFileReader {

    public static List<BasicSearchEvent> readEvents(String filename) {
        List<BasicSearchEvent> events = new ArrayList<>();

        // ===== READ ONE-TIME EVENTS =====
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                String title = parts[1].trim();
                LocalDateTime start = LocalDateTime.parse(parts[3].trim());

                events.add(new BasicSearchEvent(title, start));
            }

        } catch (Exception e) {
            System.out.println("Error reading event.csv");
        }

        // ===== READ RECURRING EVENTS =====
        RecurringEventsGenerator gen = new RecurringEventsGenerator();
        Object[][] recurring = gen.generateRecurringEvents();
        int total = gen.getCount();

        for (int i = 0; i < total; i++) {
            String title = String.valueOf(recurring[i][0]);
            LocalDateTime start = (LocalDateTime) recurring[i][2];

            events.add(new BasicSearchEvent(title, start));
        }

        return events;
    }
}
