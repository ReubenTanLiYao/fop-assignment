package FOP_Assignment;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class EventUpdateDelete {

    private static final String FILE_PATH = "data" + File.separator + "event.csv";

    // ===== MAIN MENU =====
    public static void menu() {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Update / Delete Event ===");
            System.out.println("1. Update event");
            System.out.println("2. Delete event");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    updateEvent();
                    break;
                case "2":
                    deleteEvent(sc);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== UPDATE EVENT =====
    public static void updateEvent() {
    
        Scanner sc = new Scanner(System.in);
        String filePath = "data" + File.separator + "event.csv";
        File inputFile = new File(filePath);
        File tempFile = new File("data" + File.separator + "event_temp.csv");

        System.out.println("===== UPDATE EVENT =====");
        listEvents();
        System.out.println("Enter Event ID to update:");
        int targetId;
        try {
            targetId = Integer.parseInt(sc.nextLine()); // IMPORTANT
        } catch (Exception e) {
            System.out.println("Invalid ID.");
            return;
        }

        boolean found = false;

        try (
            Scanner fileScanner = new Scanner(inputFile);
            PrintWriter pw = new PrintWriter(new FileOutputStream(tempFile))
        ) {

            // Copy header
            if (fileScanner.hasNextLine()) {
                pw.println(fileScanner.nextLine());
            }

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                int eventId = Integer.parseInt(parts[0].trim());

                if (eventId == targetId) {

                    found = true;

                    String title = parts[1].trim();
                    String desc = parts[2].trim();
                    LocalDateTime start = LocalDateTime.parse(parts[3].trim());
                    LocalDateTime end = LocalDateTime.parse(parts[4].trim());

                    System.out.println("\nCurrent Event Details:");
                    System.out.println("Title       : " + title);
                    System.out.println("Description : " + desc);
                    System.out.println("Start       : " + start);
                    System.out.println("End         : " + end);

                    // ---- UPDATE TITLE ----
                    System.out.println("\nEnter new title (press Enter to keep current):");
                    String input = sc.nextLine().trim();
                    if (!input.isEmpty()) {
                        title = input;
                    }

                    // ---- UPDATE DESCRIPTION ----
                    System.out.println("Enter new description (press Enter to keep current):");
                    input = sc.nextLine().trim();
                    if (!input.isEmpty()) {
                        desc = input;
                    }

                    // ---- UPDATE START ----
                    System.out.println("Enter new start date & time (yyyy-MM-ddTHH:mm:ss) (press Enter to keep current):");
                    input = sc.nextLine().trim();
                    if (!input.isEmpty()) {
                        try {
                            start = LocalDateTime.parse(input);
                        } catch (Exception e) {
                            System.out.println("Invalid start date format. Keeping current.");
                        }
                    }

                    // ---- UPDATE END ----
                    System.out.println("Enter new end date & time (yyyy-MM-ddTHH:mm:ss) (press Enter to keep current):");
                    input = sc.nextLine().trim();
                    if (!input.isEmpty()) {
                        try {
                            end = LocalDateTime.parse(input);
                        } catch (Exception e) {
                            System.out.println("Invalid end date format. Keeping current.");
                        }
                    }

                    // ---- VALIDATION ----
                    if (!end.isAfter(start)) {
                        System.out.println("Error: End time must be after start time.");
                        System.out.println("Update cancelled.");
                        pw.println(line); // write original
                        continue;
                    }

                    // ---- SAVE UPDATED EVENT ----
                    pw.printf("%d,%s,%s,%s,%s%n", eventId, title, desc, start, end);

                    System.out.println("Event updated successfully!\n");

                } else {
                    pw.println(line); // copy unchanged
                }
            }

        } catch (Exception e) {
            System.out.println("Error updating event.");
            return;
        }

        if (!found) {
            System.out.println("Event ID not found.");
            tempFile.delete();
            return;
        }

        // Replace old file
        inputFile.delete();
        tempFile.renameTo(inputFile);
    }


    // ===== DELETE EVENT (UPDATED) =====
    private static void deleteEvent(Scanner sc) {

        listEvents();

        System.out.print("Enter event ID to delete: ");
        int targetId;
        try {
            targetId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
            return;
        }

        File original = new File(FILE_PATH);
        File temp = new File("data" + File.separator + "event_temp.csv");

        boolean deleted = false;

        try (
            Scanner fileSc = new Scanner(original);
            PrintWriter pw = new PrintWriter(new FileOutputStream(temp))
        ) {
            // Copy header
            if (fileSc.hasNextLine())
                pw.println(fileSc.nextLine());

            while (fileSc.hasNextLine()) {
                String line = fileSc.nextLine();
                // Split by comma to check ID
                String[] e = line.split(",");

                if (e.length > 0) {
                    try {
                        int id = Integer.parseInt(e[0].trim());
                        if (id == targetId) {
                            deleted = true; // Skip this line (delete logic)
                            continue; 
                        }
                    } catch (NumberFormatException ex) {
                        // ignore malformed lines
                    }
                }
                // Write line if not deleted
                pw.println(line);
            }

        } catch (Exception e) {
            System.out.println("Error deleting event: " + e.getMessage());
            return;
        }

        replaceFile(temp, original);

        if (deleted) {
            System.out.println("Event deleted from event.csv.");
            // Trigger deletion of linked recurrence rule
            deleteRecurrentRule(targetId);
        } else {
            System.out.println("Event ID not found.");
            temp.delete(); // Clean up temp file if nothing happened
        }
    }

    // ===== NEW HELPER METHOD: DELETE RECURRENT RULE =====
    private static void deleteRecurrentRule(int targetId) {
        
        File recurrentFile = new File("data" + File.separator + "recurrent.csv");
        File tempRecurrent = new File("data" + File.separator + "recurrent_temp.csv");

        // If recurrent.csv doesn't exist, there is nothing to delete
        if (!recurrentFile.exists()) {
            return;
        }

        boolean ruleDeleted = false;

        try (
            Scanner fileSc = new Scanner(recurrentFile);
            PrintWriter pw = new PrintWriter(new FileOutputStream(tempRecurrent))
        ) {
            // Copy header if it exists
            if (fileSc.hasNextLine()) {
                pw.println(fileSc.nextLine());
            }

            while (fileSc.hasNextLine()) {
                String line = fileSc.nextLine();
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        
                        // If ID matches, we skip writing this line (effectively deleting it)
                        if (id == targetId) {
                            ruleDeleted = true;
                            continue; 
                        }
                    } catch (NumberFormatException e) {
                        // Ignore parsing errors for safety
                    }
                }
                
                // Write the line if it wasn't the target ID
                pw.println(line);
            }

        } catch (IOException e) {
            System.out.println("Warning: Could not update recurrent.csv - " + e.getMessage());
            tempRecurrent.delete();
            return;
        }

        // Only replace the file if we actually changed something
        if (ruleDeleted) {
            replaceFile(tempRecurrent, recurrentFile);
            System.out.println("Associated recurrence rule deleted.");
        } else {
            // Clean up temp file if no changes were made
            tempRecurrent.delete();
        }
    }

    // ===== LIST EVENTS =====
    private static void listEvents() {

        try (Scanner sc = new Scanner(new File(FILE_PATH))) {

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
            System.out.println("Unable to read events.");
        }
    }

    // ===== READ DATE & TIME =====
    private static LocalDateTime readDateTime(
            Scanner sc, String label) {

        DateTimeFormatter df =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (true) {
            try {
                System.out.print(label +
                        " date & time (yyyy-MM-dd HH:mm): ");
                return LocalDateTime.parse(sc.nextLine(), df);
            } catch (Exception e) {
                System.out.println("Invalid format.");
            }
        }
    }

    // ===== REPLACE ORIGINAL FILE =====
    private static void replaceFile(File temp, File original) {

        if (!original.delete()) {
            System.out.println("Could not delete old file.");
            return;
        }

        if (!temp.renameTo(original)) {
            System.out.println("Could not rename temp file.");
        }
    }
}

