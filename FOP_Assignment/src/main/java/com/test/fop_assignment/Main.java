/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.fop_assignment;


import java.util.Scanner;

/**
 *
 * @author User
 */
public class Main {
    
    public static void main(String[] args) {
        FolderCreation.createFolder();
        FileCreation.createFile();
        BackupRestore.backup();
        
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n Main Menu:");
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Calendar View");
            System.out.println("2. Add Event");
            System.out.println("3. Add Recurring Event");
            System.out.println("4. Basic Search");
            System.out.println("5. Advanced Search");
            System.out.println("6. Show Event Statistics");
            System.out.println("7. Detect Conflicts");
            System.out.println("8. Add Details for Events");
            System.out.println("9. Update or Delete Event");
            
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Exiting...");
                break;
            }

            switch (choice) {
                case "1":
                    while (true) {
                        CalendarView.calendarSelection();
                        if (!askYesNo(sc, "Run Calendar View again?")) break;
                    }
                    break;

                case "2":
                    while (true) {
                        EventCreation.createEvent();
                        if (!askYesNo(sc, "Create Another Event?")) break;
                    }
                    break;

                case "3":
                    while (true) {
                        RecurringEvents.createRecurringEvents();
                        if (!askYesNo(sc, "Create Another Recurring Event?")) break;
                    }
                    break;

                case "4":
                    while (true) {
                        BasicSearch.basicSearch();
                        if (!askYesNo(sc, "Search Events again?")) break;
                    }
                    break;

                case "5":
                    while (true) {
                        AdvancedSearch.searchMenu();
                        if (!askYesNo(sc, "Search Events again?")) break;
                    }
                    break;
                case "6":
                    while (true) {
                        EventStatistics.showStats();
                        if (!askYesNo(sc, "Show Stats Again?")) break;
                    }
                    break;
                case "7" : 
                    while (true) {
                        ConflictDetection.getDate();
                        if (!askYesNo(sc, "Check more conflicts?")) break;
                    }
                    break;
                    
                case "8" : 
                    while (true) {
                        AdditionalFields.addInfo();
                        if (!askYesNo(sc, "Add details to other events?")) break;
                    }
                    break;
                case "9" : 
                    while (true) {
                        EventUpdateDelete.menu();
                        if (!askYesNo(sc, "Update//Delete more events?")) break;
                    }
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static boolean askYesNo(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Invalid input.");
        }
    }
}