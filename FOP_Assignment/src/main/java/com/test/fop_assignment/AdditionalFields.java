package com.test.fop_assignment;

import java.io.*;
import java.util.Scanner;

public class AdditionalFields {

    private static String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;

    public static void addInfo() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n\t\t   Additional Fields:");
        System.out.println("--------------------------------------------------------");
        listEvents();

        System.out.print("\nEnter Event ID to add details to: ");
        String id = sc.nextLine().trim();

        // 2. Collect additional info (Optional inputs)
        System.out.print("Enter Location (Press Enter to skip): ");
        String location = sc.nextLine().trim();
        
        System.out.print("Enter Type (e.g., Work, Personal - Press Enter to skip): ");
        String type = sc.nextLine().trim();
        
        System.out.print("Enter Attendees (Names - Press Enter to skip): ");
        String attendees = sc.nextLine().trim();

        // 3. Save to additional.csv
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(folderPath + "additional.csv", true))) {
            // eventId, field1 (Location), field2 (Type), field3 (Attendees)
            pw.printf("%s,%s,%s,%s%n", 
                id, 
                location.isEmpty() ? "N/A" : location, 
                type.isEmpty() ? "N/A" : type, 
                attendees.isEmpty() ? "N/A" : attendees
            );
            System.out.println("Additional information saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving additional fields.");
        }
    }

    private static void listEvents() {
        String eventFilePath = folderPath + "event.csv";
        File file = new File(eventFilePath);

        if (!file.exists()) {
            System.out.println("No events file found.");
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            System.out.println("\nAvailable Events:");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                
                String[] e = line.split(",");
                System.out.println("ID: " + e[0].trim() + " | " + e[1].trim() + " | " + e[3].trim());
            }
        } catch (Exception e) {
            System.out.println("Unable to read events file.");
        }
    }
}
