/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.fop_assignment;

/**
 *
 * @author User
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class BasicSearch {

    public static void basicSearch() {

        String filePath = System.getProperty("user.dir") + File.separator + "data"+ File.separator + "event.csv";

        List<BasicSearchEvent> events = BasicSearchEventFileReader.readEvents(filePath);

        Scanner sc = new Scanner(System.in);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d-M-yyyy");


        int choice;

        // ===== MENU INPUT LOOP =====
        while (true) {
            System.out.println("\n\t\t   Basic Search:");
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Search by date");
            System.out.println("2. Search by date range");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice == 1 || choice == 2) break;
                System.out.println("Invalid choice. Please enter 1 or 2.\n");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.\n");
            }
        }

        // ===== SEARCH BY SINGLE DATE =====
        if (choice == 1) {

            LocalDate date;

            while (true) {
                System.out.print("Enter date (e.g. d-m-yyyy): ");
                try {
                    date = LocalDate.parse(sc.nextLine().trim(), df);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date. Please try again.\n");
                }
            }

            System.out.println("\nEvents on " + date + ":");
            boolean found = false;

            for (BasicSearchEvent e : events) {
                if (e.getStartDateTime().toLocalDate().equals(date)) {
                    System.out.println("- " + e.getTitle()
                            + " (" + e.getStartDateTime().toLocalTime() + ")");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No events found.");
            }
        }

        // ===== SEARCH BY DATE RANGE =====
        else {

            LocalDate start;
            LocalDate end;

            while (true) {
                try {
                    System.out.print("Enter start date (e.g. d-m-yyyy): ");
                    start = LocalDate.parse(sc.nextLine().trim(), df);

                    System.out.print("Enter end date (e.g. d-m-yyyy): ");
                    end = LocalDate.parse(sc.nextLine().trim(), df);

                    if (end.isBefore(start)) {
                        System.out.println("End date cannot be before start date.\n");
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date. Please try again.\n");
                }
            }

            System.out.println("\nEvents from " + start + " to " + end + ":");
            boolean found = false;

            for (BasicSearchEvent e : events) {
                LocalDate eventDate = e.getStartDateTime().toLocalDate();
                if (!eventDate.isBefore(start) && !eventDate.isAfter(end)) {
                    System.out.println("- " + e.getTitle()
                            + " (" + eventDate + ")");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No events found.");
            }
        }
    }
}
