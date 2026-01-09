/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author LSJ
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventFileReader {

    public static List<Event> readEvents(String filename) {
        List<Event> events = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            // skip header (5 lines)
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();

            while (true) {
                String idLine = br.readLine();
                if (idLine == null) break;

                String title = br.readLine();
                br.readLine(); // description
                String startLine = br.readLine();
                br.readLine(); // endDateTime

                LocalDateTime start = LocalDateTime.parse(startLine);
                events.add(new Event(title, start));
            }

        } catch (Exception e) {
            System.out.println("Error reading event.csv");
            e.printStackTrace();
        }
        return events;
    }
}