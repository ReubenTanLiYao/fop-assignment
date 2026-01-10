/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

/**
 *
 * @author reube
 */
import java.io.*;
import java.util.Scanner;
import java.time.*;
public class RecurringEvents {

    // ===== PUBLIC ENTRY POINT =====
    public static void createRecurringEvents() {
        Scanner sc = new Scanner(System.in);

        String recurrentFilePath = System.getProperty("user.dir")+File.separator + "data" + File.separator + "recurrent.csv";

        while (true) {

            listEvents();

            System.out.println("Enter event ID to make recurring:");
            int eventId = Integer.parseInt(sc.nextLine());

            System.out.println("Enter recurrence interval (e.g. 1d, 1w):");
            String interval = sc.nextLine();

            System.out.println("Repeat by:");
            System.out.println("1. Number of times");
            System.out.println("2. Until a date");
            String option = sc.nextLine();

            int times = 0;
            String endDate = "0";

            if (option.equals("1")) {
                System.out.println("Enter number of times:");
                times = Integer.parseInt(sc.nextLine());
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
                            System.out.println("Invalid end date. End date cannot be before event start date (" + eventStartDate + ").");
                        } else {
                            break; // valid
                        }

                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }
                }
            }
            else {
                System.out.println("Invalid option.");
                continue;
            }

            try (PrintWriter pw = new PrintWriter(new FileOutputStream(recurrentFilePath, true))) {
                pw.printf("%d,%s,%d,%s%n", eventId, interval, times, endDate);
                System.out.println("Recurring rule saved");
            } catch (Exception e) {
                System.out.println("Error saving recurring rule");
            }

            System.out.println("Create another recurring event? (y/n)");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    // ===== DISPLAY EXISTING EVENTS =====
    private static void listEvents() {

        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine(); // skip header

            System.out.println("\nAvailable Events:");
            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");
                System.out.println(
                        "ID: " + e[0].trim() +
                        " | " + e[1].trim() +
                        " | " + e[3].trim()
                );
            }

        } catch (Exception e) {
            System.out.println("Unable to read events file.");
        }
    }
    
    private static LocalDate getEventStartDate(int eventId) {

        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");

                if (Integer.parseInt(e[0].trim()) == eventId) {
                    return LocalDateTime
                            .parse(e[3].trim())
                            .toLocalDate();
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading event start date.");
        }

        return null;
    }


}

