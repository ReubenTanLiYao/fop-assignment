package com.test.calendar;

import java.io.*;

public class FileCreation {

    public static void createFile() {
        String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
        
        // Define files and add headers
        setupFile(folderPath  + "event.csv", "eventId,title,description,startDateTime,endDateTime");
        setupFile(folderPath  + "recurrent.csv", "eventId,recurrentInterval,recurrentTimes,recurrentEndDate");
        setupFile(folderPath  + "additional.csv", "eventId,Location,Type,Attendees");
    }

    private static void setupFile(String path, String header) {
        File file = new File(path);
        
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    // Write header immediately after creation
                    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                        pw.println(header);
                    }
                    System.out.println("Created and initialized: " + file.getName());
                }
            } catch (IOException e) {
                System.out.println("Error creating " + file.getName() + ": " + e.getMessage());
            }
        } else {
            System.out.println(file.getName() + " already exists.");
        }
    }
}