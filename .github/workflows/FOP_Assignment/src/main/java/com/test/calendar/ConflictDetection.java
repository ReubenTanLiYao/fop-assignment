package com.test.calendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConflictDetection {

    private static String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;

    public static void getDate() {

        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");

        LocalDate startDate;
        LocalDate endDate;

        System.out.println("\n\t\t   Showing Conflicts:");
        System.out.println("--------------------------------------------------------");
        while (true) {
            try {
                System.out.print("Enter start date (e.g. 1-1-2026): ");
                startDate = LocalDate.parse(sc.nextLine().trim(), formatter);

                System.out.print("Enter end date (e.g. 1-1-2026): ");
                endDate = LocalDate.parse(sc.nextLine().trim(), formatter);

                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    continue;
                }

                break;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use d-m-yyyy.");
            }
        }

        // convert date → datetime
        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to = endDate.atTime(23, 59, 59);

        String[][] conflicts = findConflicts(from, to);

    System.out.println("\n Conflicting Events:");
    

    System.out.println("-".repeat(140));
    System.out.printf("%-5s %-65s %-65s%n", "No.", "Event 1 (Details)", "Event 2 (Details)");
    System.out.println("-".repeat(140));

    DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy");

    for (int i = 0; i < conflicts.length; i++) {
        // Build Event 1 String
        String e1Details = String.format("%s (%s - %s)", 
                conflicts[i][0], 
                LocalDateTime.parse(conflicts[i][2]).format(tf), 
                LocalDateTime.parse(conflicts[i][3]).format(tf));

        // Build Event 2 String
        String e2Details = String.format("%s (%s - %s)", 
                conflicts[i][1], 
                LocalDateTime.parse(conflicts[i][4]).format(tf), 
                LocalDateTime.parse(conflicts[i][5]).format(tf));

       
        System.out.printf("%-5d %-65s %-65s%n", i + 1, e1Details, e2Details);
    }

    System.out.println("-".repeat(140));
    System.out.println("Total conflicting events found: " + conflicts.length);
    }

    
    public static String[][] findConflicts( LocalDateTime from ,LocalDateTime to) {

        // ---------- READ NORMAL EVENTS ----------
        Object[][] normalEvents = new Object[200][6];
        int normalCount = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(folderPath + "event.csv"));
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String title = parts[1].trim();
                LocalDateTime start =
                        LocalDateTime.parse(parts[3].trim());
                LocalDateTime end =
                        LocalDateTime.parse(parts[4].trim());

                // keep only events that touch the range
                if (start.isBefore(to) && end.isAfter(from)) {
                    normalEvents[normalCount][0] = title;
                    normalEvents[normalCount][1] = start;
                    normalEvents[normalCount][2] = end;
                    normalCount++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //GENERATE RECURRING EVENTS
        RecurringEventsGenerator generator = new RecurringEventsGenerator();
        Object[][] recurringEvents = generator.generateRecurringEvents();
        int recurringCount = generator.getCount();

        
        String[][] conflicts = new String[200][6];
        int conflictCount = 0;

        // normal vs normal
        for (int i = 0; i < normalCount; i++) {
            for (int j = i + 1; j < normalCount; j++) {

                LocalDateTime aStart =(LocalDateTime) normalEvents[i][1];
                LocalDateTime aEnd = (LocalDateTime) normalEvents[i][2];
                LocalDateTime bStart = (LocalDateTime) normalEvents[j][1];
                LocalDateTime bEnd = (LocalDateTime) normalEvents[j][2];

        if (aStart.isBefore(bEnd) && aEnd.isAfter(bStart)) {
            conflicts[conflictCount][0] = (String) normalEvents[i][0];
            conflicts[conflictCount][1] = (String) normalEvents[j][0];
            conflicts[conflictCount][2] = aStart.toString();
            conflicts[conflictCount][3] = aEnd.toString(); // Store Event 1 End
            conflicts[conflictCount][4] = bStart.toString(); // Store Event 2 Start
            conflicts[conflictCount][5] = bEnd.toString();   // Store Event 2 End
            conflictCount++;
        }

            }
        }

        // recurring vs normal
        for (int i = 0; i < recurringCount; i++) {

            String rTitle =
                    (String) recurringEvents[i][0];
            LocalDateTime rStart =
                    (LocalDateTime) recurringEvents[i][2];
            LocalDateTime rEnd =
                    (LocalDateTime) recurringEvents[i][3];

            if (rStart.isAfter(to) || rEnd.isBefore(from)) {
                continue;
            }

            for (int j = 0; j < normalCount; j++) {

                LocalDateTime nStart =
                        (LocalDateTime) normalEvents[j][1];
                LocalDateTime nEnd =
                        (LocalDateTime) normalEvents[j][2];

            if (rStart.isBefore(nEnd) && rEnd.isAfter(nStart)) {
                conflicts[conflictCount][0] = rTitle;
                conflicts[conflictCount][1] = (String) normalEvents[j][0];
                conflicts[conflictCount][2] = rStart.toString();
                conflicts[conflictCount][3] = rEnd.toString();   // Store Event 1 End
                conflicts[conflictCount][4] = nStart.toString(); // Store Event 2 Start
                conflicts[conflictCount][5] = nEnd.toString();   // Store Event 2 End
                conflictCount++;
            }
            }
        }

        
        String[][] result = new String[conflictCount][4]; 
        for (int i = 0; i < conflictCount; i++) {
            result[i] = conflicts[i];
        }
        return result;
    }
}
