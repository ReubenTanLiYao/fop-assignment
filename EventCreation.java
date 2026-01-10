package test.java;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EventCreation {
    // Shared scanner to prevent input stream conflicts
    private static final Scanner sc = new Scanner(System.in);

    public static void createEvent() {
        if (!Event.askYesNo(sc, "Create New Event?")) {
            return;
        }

        while (true) {
            System.out.println("\n\t\t   Creating Events:");
            System.out.println("--------------------------------------------------------");
            
            Event e = new Event(sc);
            e.saveEvent();

            if (!Event.askYesNo(sc, "Create another Event?\n")) {
                break;
            }
        }
    }
}

class Event {
    private static final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "event.csv";
    private static int idCounter = Event.getLastEventId();

    private final int eventId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Event(Scanner sc) {
        this.eventId = ++idCounter;
        initializeInputs(sc);
    }

    private void initializeInputs(Scanner sc) {
        // 1. Title Input (Removing commas to protect CSV structure)
        while (true) {
            System.out.println("Enter Title of Event:");
            this.title = sc.nextLine().trim().replace(",", ";"); 
            if (!this.title.isEmpty()) break;
            System.out.println("Title cannot be empty.\n");
        }

        // 2. Description Input
        while (true) {
            System.out.println("Enter Description of Event:");
            this.description = sc.nextLine().trim().replace(",", ";");
            if (!this.description.isEmpty()) break;
            System.out.println("Description cannot be empty.\n");
        }

        // 3. Date and Time logic
        DateTimeFormatter dateDf = DateTimeFormatter.ofPattern("d-M-yyyy");
        DateTimeFormatter timeDf = DateTimeFormatter.ofPattern("H:mm");
        
        LocalDate eventDate = null;
        while (eventDate == null) {
            try {
                System.out.println("Enter Date (dd-mm-yyyy):");
                eventDate = LocalDate.parse(sc.nextLine(), dateDf);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use day-month-year (e.g., 10-01-2026).");
            }
        }

        while (true) {
            try {
                System.out.println("Enter Start Time (HH:mm):");
                LocalTime startTime = LocalTime.parse(sc.nextLine(), timeDf);
                
                System.out.println("Enter End Time (HH:mm):");
                LocalTime endTime = LocalTime.parse(sc.nextLine(), timeDf);

                this.startDateTime = LocalDateTime.of(eventDate, startTime);
                this.endDateTime = LocalDateTime.of(eventDate, endTime);

                if (validStartEnd(startDateTime, endDateTime)) {
                    break;
                } else {
                    System.out.println("Error: End Time must be after Start Time.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Use 24h format (e.g., 14:30).");
            }
        }
    }

    public void saveEvent() {
        // Use try-with-resources for automatic closing
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filePath, true))) {
            pw.printf("%d, %s, %s, %s, %s%n", 
                eventId, title, description, startDateTime, endDateTime);
            System.out.println("Event Saved Successfully (ID: " + eventId + ")\n");
        } catch (IOException e) {
            System.err.println("Error saving event: " + e.getMessage());
        }
    }

    public static int getLastEventId() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) return 0;

        String lastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) lastLine = currentLine;
            }
            if (!lastLine.isEmpty()) {
                return Integer.parseInt(lastLine.split(",")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Could not recover last ID, starting from 0.");
        }
        return 0;
    }

    public static boolean validStartEnd(LocalDateTime start, LocalDateTime end) {
        return end.isAfter(start);
    }

    public static boolean askYesNo(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Invalid input.");
        }
    }
}