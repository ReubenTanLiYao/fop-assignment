package com.test.calendar;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdvancedSearch {

    private static Object[][] recurringEvents;
    private static int recurringCount;
    private static String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    
    private static final DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("d-M-yyyy");

    static {
        RecurringEventsGenerator gen = new RecurringEventsGenerator();
        recurringEvents = gen.generateRecurringEvents();
        recurringCount = gen.getCount();
    }

    public static void searchMenu() {
        Scanner sc = new Scanner(System.in);

        // Removed the infinite loop so it returns to the Main Menu
        System.out.println("\n\t\t   Advanced Search:");
        System.out.println("--------------------------------------------------------");
        System.out.println("1. Search by title keyword");
        System.out.println("2. Search by date range");
        System.out.println("3. Search by duration");
        System.out.println("4. Search by Category (Additional Fields)");
        System.out.println("5. Search by Attendees (Additional Fields)");
        System.out.print("Choose option (or press Enter to return): ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1": searchByTitle(sc); break;
            case "2": searchByDateRange(sc); break;
            case "3": searchByDuration(sc); break;
            case "4": searchByAdditionalField(sc, "Category", 2); break; // Field 2 in additional.csv
            case "5": searchByAdditionalField(sc, "Attendees", 3); break; // Field 3 in additional.csv
            default: System.out.println("Returning...");
        }
    }

    private static void searchByTitle(Scanner sc) {
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().toLowerCase();

        readAndFilterEvents(event ->
            event[1].toLowerCase().contains(keyword)
        );
    }

    private static void searchByDateRange(Scanner sc) {
        try {
            System.out.print("Enter start date (d-m-yyyy): ");
            LocalDate from = LocalDate.parse(sc.nextLine().trim(), dFmt);

            System.out.print("Enter end date (d-m-yyyy): ");
            LocalDate to = LocalDate.parse(sc.nextLine().trim(), dFmt);

            readAndFilterEvents(event -> {
                // event[3] is stored as yyyy-MM-ddTHH:mm in event.csv
                LocalDate eventDate = LocalDateTime.parse(event[3].trim()).toLocalDate();
                return !eventDate.isBefore(from) && !eventDate.isAfter(to);
            });

        } catch (Exception e) {
            System.out.println("Invalid date format. Use d-m-yyyy.");
        }
    }

    private static void searchByDuration(Scanner sc) {
        System.out.println("1. Short events (< 1 hour)");
        System.out.println("2. Long events (>= 1 hour)");
        System.out.print("Choose option: ");

        String option = sc.nextLine().trim();

        readAndFilterEvents(event -> {
            LocalDateTime start = LocalDateTime.parse(event[3].trim());
            LocalDateTime end = LocalDateTime.parse(event[4].trim());

            // Using Duration for better accuracy
            long minutes = Duration.between(start, end).toMinutes();

            if (option.equals("1")) return minutes < 60;
            if (option.equals("2")) return minutes >= 60;

            return false;
        });
    }

    private static void searchByAdditionalField(Scanner sc, String fieldName, int colIndex) {
        System.out.print("Enter " + fieldName + " keyword: ");
        String keyword = sc.nextLine().toLowerCase().trim();
        boolean found = false;

        try (Scanner addSc = new Scanner(new File(folderPath + "additional.csv"))) {
            if (addSc.hasNextLine()) addSc.nextLine(); // skip header

            while (addSc.hasNextLine()) {
                String[] addParts = addSc.nextLine().split(",");
                if (addParts.length <= colIndex) continue;

                // Check if the keyword exists in the specific column
                if (addParts[colIndex].toLowerCase().contains(keyword)) {
                    String targetId = addParts[0].trim();
                    
                    // BEGIN INNER LOOP: Find the title/time in event.csv
                    findAndPrintBasicEvent(targetId);
                    found = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading files.");
        }

        if (!found) System.out.println("No matching events found.");
    }
    private static void findAndPrintBasicEvent(String targetId) {
        try (Scanner eventSc = new Scanner(new File(folderPath + "event.csv"))) {
            if (eventSc.hasNextLine()) eventSc.nextLine(); // skip header

            while (eventSc.hasNextLine()) {
                String[] eventParts = eventSc.nextLine().split(",");
                String currentId = eventParts[0].trim();

                if (currentId.equals(targetId)) {
                    printEvent(eventParts[1], eventParts[2], eventParts[3], eventParts[4]);
                    return; 
                }
            }
        } catch (Exception e) {
            System.out.println("Could not find basic info for ID: " + targetId);
        }
    }
    
    private static void readAndFilterEvents(EventCondition condition) {
            boolean found = false;
            String eventFilePath = folderPath + "event.csv";

            try (Scanner sc = new Scanner(new File(eventFilePath))) {
                if (sc.hasNextLine()) sc.nextLine(); 
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.trim().isEmpty()) continue;
                    String[] e = line.split(",");
                    if (condition.check(e)) {
                        printEvent(e[1], e[2], e[3], e[4]);
                        found = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading event.csv");
            }
        for (int i = 0; i < recurringCount; i++) {
            String title = recurringEvents[i][0].toString();
            String desc  = recurringEvents[i][1].toString();
            String start = recurringEvents[i][2].toString();
            String end   = recurringEvents[i][3].toString();

            String[] fakeEvent = { "", title, desc, start, end };

            if (condition.check(fakeEvent)) {
                printEvent(title, desc, start, end);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching events found.");
        }
    }

    private static void printEvent(String title, String desc, String start, String end) {
        // Formatting for display
        LocalDateTime s = LocalDateTime.parse(start.trim());
        LocalDateTime e = LocalDateTime.parse(end.trim());
        DateTimeFormatter displayFmt = DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy");

        System.out.printf("Event: %-15s | Desc: %-15s | From: %s | To: %s%n", 
                title.trim(), desc.trim(), s.format(displayFmt), e.format(displayFmt));
    }

    private interface EventCondition {
        boolean check(String[] event);
    }
}