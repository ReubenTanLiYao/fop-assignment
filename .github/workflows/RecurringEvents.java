/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

import java.io.*;
import java.util.Scanner;
import java.time.*;

public class RecurringEvents {

    public static void createRecurringEvents() {
        Scanner sc = new Scanner(System.in);

        String recurrentFilePath = "data" + File.separator + "recurrent.csv";
        new File("data").mkdirs(); // ensure folder exists

        while (true) {

            listEvents();

            // ===== EVENT ID INPUT =====
            int eventId;
            while (true) {
                System.out.println("Enter event ID to make recurring:");
                String input = sc.nextLine();
                if (!eventExists(Integer.parseInt(input))) {
                    System.out.println("Event ID does not exist. Please choose a valid event.");
                    continue;
                }
                try {
                    eventId = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid event ID. Please enter a number.");
                }
            }

            // ===== INTERVAL INPUT =====
            String interval;
            while (true) {
                System.out.println("Enter recurrence interval (e.g. 1d, 1w):");
                interval = sc.nextLine().trim();

                if (interval.matches("\\d+[dw]")) {
                    break;
                } else {
                    System.out.println("Invalid interval format. Example: 1d or 2w");
                }
            }

            // ===== OPTION MENU =====
            System.out.println("Repeat by:");
            System.out.println("1. Number of times");
            System.out.println("2. Until a date");

            String option = sc.nextLine();

            int times = 0;
            String endDate = "0";

            if (option.equals("1")) {

                while (true) {
                    System.out.println("Enter number of times:");
                    try {
                        times = Integer.parseInt(sc.nextLine());

                        if (times > 0) break;
                        System.out.println("Times must be greater than 0.");

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                }

            } 
            else if (option.equals("2")) {

                LocalDate eventStartDate = getEventStartDate(eventId);

                if (eventStartDate == null) {
                    System.out.println("Invalid event ID.");
                    continue;
                }

                while (true) {
                    System.out.println("Enter end date (yyyy-MM-dd):");
                    endDate = sc.nextLine();

                    try {
                        LocalDate recurEnd = LocalDate.parse(endDate);

                        if (recurEnd.isBefore(eventStartDate)) {
                            System.out.println("End date cannot be before event start date (" + eventStartDate + ").");
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }
                }

            } 
            else {
                System.out.println("Invalid option. Choose 1 or 2.");
                continue;
            }

            // ===== SAVE TO FILE =====
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(recurrentFilePath, true))) {
                pw.printf("%d,%s,%d,%s%n", eventId, interval, times, endDate);
                System.out.println("Recurring rule saved.");
            } catch (Exception e) {
                System.out.println("Error saving recurring rule.");
            }

            System.out.println("Create another recurring event? (y/n)");
            if (!sc.nextLine().equalsIgnoreCase("y")) break;
        }
    }

    // ===== DISPLAY EVENTS =====
    private static void listEvents() {
        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine();

            System.out.println("\nAvailable Events:");
            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");
                System.out.println("ID: " + e[0].trim() + " | " + e[1].trim() + " | " + e[3].trim());
            }

        } catch (Exception e) {
            System.out.println("Unable to read events file.");
        }
    }

    // ===== GET EVENT START DATE =====
    private static LocalDate getEventStartDate(int eventId) {

        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine();

            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");

                if (Integer.parseInt(e[0].trim()) == eventId) {
                    return LocalDateTime.parse(e[3].trim()).toLocalDate();
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading event start date.");
        }

        return null;
    }
    
    private static boolean eventExists(int eventId) {

        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");

                if (Integer.parseInt(e[0].trim()) == eventId) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Error checking event existence.");
        }

        return false;
    }
}


