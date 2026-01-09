package util;

import model.Event;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvEventUtil {

    private static final String FILE_NAME = "event.csv";

    public static List<Event> readEvents() {
        List<Event> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String desc = parts[2];
                LocalDateTime start = LocalDateTime.parse(parts[3]);
                LocalDateTime end = LocalDateTime.parse(parts[4]);

                events.add(new Event(id, title, desc, start, end));
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file.");
        }

        return events;
    }

    public static void writeEvents(List<Event> events) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Event e : events) {
                pw.println(
                    e.getEventId() + "," +
                    e.getTitle() + "," +
                    e.getDescription() + "," +
                    e.getStart() + "," +
                    e.getEnd()
                );
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV file.");
        }
    }
}
