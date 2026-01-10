package FOP_Assignment;

import FOP_Assignment.Event;
import FOP_Assignment.RecurringEventsGenerator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConflictDetection{
    
    private static final String EVENT_FILE = "event.csv";


    // Read events from CSV file
    public static List<Event> readEventsFromCSV() {
        List<Event> eventList = new ArrayList<>();

        try{
            BufferedReader buffer = new BufferedReader(new FileReader(EVENT_FILE));

            String line;

            // Skip header line
            buffer.readLine(); 

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                int eventId = Integer.parseInt(data[0]);
                String title = data[1];
                String description = data[2];
                LocalDateTime start = LocalDateTime.parse(data[3]);
                LocalDateTime end = LocalDateTime.parse(data[4]);

                //Add events to event list
                eventList.add(new Event(eventId, title, description, start, end));
            }
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        return eventList;
    }

    //Add Recurring Events to the List
    public static List<Event> readRecurringEvents(){

        List<Event> recurringEvents = new ArrayList<>();

        RecurringEventsGenerator gen = new RecurringEventsGenerator();
        Object[][] data = gen.generateRecurringEvents();
        int total = gen.getCount();

        for (int i = 0; i < total; i++) {
            String title = (String) data[i][0];
            String description = (String) data[i][1];
            LocalDateTime start = (LocalDateTime) data[i][2];
            LocalDateTime end = (LocalDateTime) data[i][3];

            // Use -1 to indicate generated recurring event
            recurringEvents.add(new Event(-1, title, description, start, end));
        }

        return recurringEvents;
    }

    // Detect and display conflicts
    public static void detectConflicts(List<Event> eventList) {

        int conflictCount = 0;
        boolean conflictFound = false;

        for (int i = 0; i < eventList.size(); i++) {
            for (int j = i + 1; j < eventList.size(); j++) {

                Event e1 = eventList.get(i);
                Event e2 = eventList.get(j);

                if (e1.hasConflict(e2)){
                    conflictFound = true;
                    conflictCount++;
                    System.out.println("Conflict detected between (" + conflictCount + "):");
                    System.out.println(" - " + e1.getTitle() + " (" + e1.getDescription() + ")");
                    System.out.println(" - " + e2.getTitle() + " (" + e2.getDescription() + ")");
                    System.out.println();
                }
            }
        }
        if (!conflictFound){
            System.out.println("No event conflicts detected.");
        }
        System.out.println("Total Conflict Count: " + conflictCount);
    }
}