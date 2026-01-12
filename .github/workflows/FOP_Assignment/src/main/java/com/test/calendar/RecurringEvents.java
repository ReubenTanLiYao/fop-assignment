package com.test.calendar;

import java.io.*;
import java.util.Scanner;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class RecurringEvents {

    private static String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;

    public static void createRecurringEvents() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("\n Recurring Events:");
        System.out.println("--------------------------------------------------------\n");
        //shows available IDs to the user
        listEvents();

        try {
            System.out.println("Enter event ID to make recurring:");
            String idInput = sc.nextLine().trim();
            if (idInput.isEmpty()) return;
            int eventId = Integer.parseInt(idInput);

            // Verify the event exists
            LocalDate eventStartDate = getEventStartDate(eventId);
            if (eventStartDate == null) {
                System.out.println("Invalid event ID. Event not found.");
                return;
            }

            System.out.println("Enter recurrence interval (e.g. 1d for daily, 1w for weekly):");
            String interval = sc.nextLine().trim();

            System.out.println("Repeat by:");
            System.out.println("1. Number of times");
            System.out.println("2. Until a date");
            System.out.print("Select option: ");
            String option = sc.nextLine().trim();

            int times = 0;
            String endDate = "0";

            if (option.equals("1")) {
                System.out.println("Enter number of times:");
                times = Integer.parseInt(sc.nextLine().trim());
            } 
            else if (option.equals("2")) {
                
                DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("d-m-yyyy");

                while (true) {
                    System.out.println("Enter end date (d-m-yyyy, e.g., 31-12-2026):");
                    endDate = sc.nextLine().trim();
                    try {
                        
                        LocalDate recurEnd = LocalDate.parse(endDate, dFmt);
                        
                        if (recurEnd.isBefore(eventStartDate)) {
                            System.out.println("Error: End date cannot be before event start date (" + 
                                               eventStartDate.format(dFmt) + ").");
                        } else {
                            
                            endDate = recurEnd.toString(); 
                            break; 
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format. Please use d-m-yyyy.");
                    }
                }
            } else {
                System.out.println("Invalid option.");
                return;
            }

            
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(folderPath + "recurrent.csv", true))) {
                pw.printf("%d,%s,%d,%s%n", eventId, interval, times, endDate);
                System.out.println("Recurring rule saved successfully for ID: " + eventId);
            } catch (Exception e) {
                System.out.println("Error saving recurring rule.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numbers for ID and count.");
        }
    }

    private static void listEvents() {
        String eventFilePath = folderPath + "event.csv";
        try (Scanner sc = new Scanner(new File(eventFilePath))) {
            if (sc.hasNextLine()) sc.nextLine(); 

            System.out.println("\nAvailable Events:");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] e = line.split(",");
                System.out.println("ID: " + e[0].trim() + " | Title: " + e[1].trim() + " | Start: " + e[3].trim());
            }
        } catch (Exception e) {
            System.out.println("Unable to read events file.");
        }
    }

    private static LocalDate getEventStartDate(int eventId) {
        String eventFilePath = folderPath + "event.csv";
        try (Scanner sc = new Scanner(new File(eventFilePath))) {
            if (sc.hasNextLine()) sc.nextLine(); 
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] e = line.split(",");
                if (Integer.parseInt(e[0].trim()) == eventId) {
                    return LocalDateTime.parse(e[3].trim()).toLocalDate();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}