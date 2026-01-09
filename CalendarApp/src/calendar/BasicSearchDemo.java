/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author LSJ
 */

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BasicSearchDemo {

    public static void main(String[] args) {

        List<Event> events = EventFileReader.readEvents("event.csv");
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Basic Event Search ===");
        System.out.println("1. Search by date");
        System.out.println("2. Search by date range");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine());

            System.out.println("\nEvents on " + date + ":");
            boolean found = false;

            for (Event e : events) {
                if (e.getStartDateTime().toLocalDate().equals(date)) {
                    System.out.println("- " + e.getTitle()
                            + " (" + e.getStartDateTime().toLocalTime() + ")");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No events found.");
            }

        } else if (choice == 2) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            LocalDate start = LocalDate.parse(sc.nextLine());

            System.out.print("Enter end date (YYYY-MM-DD): ");
            LocalDate end = LocalDate.parse(sc.nextLine());

            System.out.println("\nEvents from " + start + " to " + end + ":");
            boolean found = false;

            for (Event e : events) {
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

        sc.close();
    }
}