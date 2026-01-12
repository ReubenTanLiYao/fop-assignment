package com.test.fop_assignment;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class EventUpdateDelete {

    private static String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    private static final String FILE_PATH = folderPath + "event.csv";

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\t\t   Update or Delete Event:");
        System.out.println("--------------------------------------------------------");
        System.out.println("1. Update event");
        System.out.println("2. Delete event");
        System.out.print("Choose option (0 to return): ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1": updateEvent(); break;
            case "2": deleteEvent(sc); break;
            default: System.out.println("Returning...");
        }
    }

    public static void updateEvent() {
        Scanner sc = new Scanner(System.in);
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(folderPath + "event_temp.csv");

        listEvents();
        System.out.print("Enter Event ID to update: ");
        int targetId;
        try {
            targetId = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid ID.");
            return;
        }

        boolean found = false;
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("d-M-yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        try (Scanner fileScanner = new Scanner(inputFile);
             PrintWriter pw = new PrintWriter(new FileOutputStream(tempFile))) {

            if (fileScanner.hasNextLine()) pw.println(fileScanner.nextLine());

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

                    System.out.println("\nCurrent: " + title + " | " + start.format(dateFmt) + " " + start.format(timeFmt));

                    System.out.print("New Title (Enter to skip): ");
                    String input = sc.nextLine().trim();
                    if (!input.isEmpty()) title = input;

                    System.out.print("New Description (Enter to skip): ");
                    input = sc.nextLine().trim();
                    if (!input.isEmpty()) desc = input;

                    System.out.print("New Date d-m-yyyy (Enter to skip): ");
                    input = sc.nextLine().trim();
                    LocalDate d = start.toLocalDate();
                    if (!input.isEmpty()) d = LocalDate.parse(input, dateFmt);

                    System.out.print("New Start Time H:mm (Enter to skip): ");
                    input = sc.nextLine().trim();
                    LocalTime st = start.toLocalTime();
                    if (!input.isEmpty()) st = LocalTime.parse(input, timeFmt);

                    System.out.print("New End Time H:mm (Enter to skip): ");
                    input = sc.nextLine().trim();
                    LocalTime et = end.toLocalTime();
                    if (!input.isEmpty()) et = LocalTime.parse(input, timeFmt);

                    start = LocalDateTime.of(d, st);
                    end = LocalDateTime.of(d, et);

                    pw.printf("%d,%s,%s,%s,%s%n", eventId, title, desc, start, end);
                    System.out.println("Updated successfully.");
                } else {
                    pw.println(line);
                }
            }
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
            tempFile.delete();
            return;
        }

        replaceFile(tempFile, inputFile);
    }

    private static void deleteEvent(Scanner sc) {
        listEvents();
        System.out.print("Enter event ID to delete: ");
        int targetId = Integer.parseInt(sc.nextLine().trim());

        File original = new File(FILE_PATH);
        File temp = new File(folderPath + "event_temp.csv");
        boolean deleted = false;

        try (Scanner fileSc = new Scanner(original);
             PrintWriter pw = new PrintWriter(new FileOutputStream(temp))) {
            
            if (fileSc.hasNextLine()) pw.println(fileSc.nextLine());

            while (fileSc.hasNextLine()) {
                String line = fileSc.nextLine();
                if (Integer.parseInt(line.split(",")[0].trim()) == targetId) {
                    deleted = true;
                    continue;
                }
                pw.println(line);
            }
        } catch (Exception e) {
            System.out.println("Delete failed.");
            return;
        }

        replaceFile(temp, original);
        if (deleted) {
            deleteLinkedData(targetId, "recurrent.csv");
            deleteLinkedData(targetId, "additional.csv");
            System.out.println("Event and all linked data deleted.");
        }
    }

    private static void deleteLinkedData(int targetId, String fileName) {
        File file = new File(folderPath + fileName);
        if (!file.exists()) return;

        File temp = new File(folderPath + "temp_" + fileName);
        try (Scanner sc = new Scanner(file);
             PrintWriter pw = new PrintWriter(new FileOutputStream(temp))) {
            
            if (sc.hasNextLine()) pw.println(sc.nextLine());
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (Integer.parseInt(line.split(",")[0].trim()) != targetId) {
                    pw.println(line);
                }
            }
        } catch (Exception e) { }
        replaceFile(temp, file);
    }

    private static void listEvents() {
        try (Scanner sc = new Scanner(new File(FILE_PATH))) {
            if (sc.hasNextLine()) sc.nextLine();
            System.out.println("\nAvailable Events:");
            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");
                System.out.println("ID: " + e[0].trim() + " | " + e[1].trim());
            }
        } catch (Exception e) { }
    }

    private static void replaceFile(File temp, File original) {
        original.delete();
        temp.renameTo(original);
    }
}