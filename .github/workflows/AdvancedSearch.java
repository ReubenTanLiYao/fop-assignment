package FOP_Assignment;

import java.io.*;
import java.time.*;
import java.util.Scanner;

public class AdvancedSearch {

    // ===== STORED RECURRING EVENTS =====
    private static Object[][] recurringEvents;
    private static int recurringCount;
    
    // ===== LOAD RECURRING EVENTS ONCE =====
    static {
        RecurringEventsGenerator gen = new RecurringEventsGenerator();
        recurringEvents = gen.generateRecurringEvents();
        recurringCount = gen.getCount();
    }


    // ===== SEARCH MENU =====
    public static void searchMenu() {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Advanced Event Search ===");
            System.out.println("1. Search by title keyword");
            System.out.println("2. Search by date range");
            System.out.println("3. Search by duration");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    searchByTitle(sc);
                    break;
                case "2":
                    searchByDateRange(sc);
                    break;
                case "3":
                    searchByDuration(sc);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== SEARCH BY TITLE =====
    private static void searchByTitle(Scanner sc) {

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().toLowerCase();

        readAndFilterEvents(event ->
            event[1].toLowerCase().contains(keyword)
        );
    }

    // ===== SEARCH BY DATE RANGE =====
    private static void searchByDateRange(Scanner sc) {

        try {
            System.out.print("Enter start date (yyyy-mm-dd): ");
            LocalDate from = LocalDate.parse(sc.nextLine());

            System.out.print("Enter end date (yyyy-mm-dd): ");
            LocalDate to = LocalDate.parse(sc.nextLine());

            readAndFilterEvents(event -> {
                LocalDate eventDate =
                        LocalDateTime.parse(event[3]).toLocalDate();
                return !eventDate.isBefore(from)
                        && !eventDate.isAfter(to);
            });

        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    // ===== SEARCH BY DURATION =====
    private static void searchByDuration(Scanner sc) {

        System.out.println("1. Short events (< 1 hour)");
        System.out.println("2. Long events (>= 1 hour)");
        System.out.print("Choose option: ");

        String option = sc.nextLine();

        readAndFilterEvents(event -> {

            LocalDateTime start = LocalDateTime.parse(event[3]);
            LocalDateTime end = LocalDateTime.parse(event[4]);

            long minutes =
                    (end.toEpochSecond(ZoneOffset.UTC)
                   - start.toEpochSecond(ZoneOffset.UTC)) / 60;

            if (option.equals("1")) return minutes < 60;
            if (option.equals("2")) return minutes >= 60;

            return false;
        });
    }

    // ===== CORE MERGED SEARCH =====
    private static void readAndFilterEvents(EventCondition condition) {

        boolean found = false;

        // ===== NORMAL EVENTS (event.csv) =====
        String eventFilePath = "data" + File.separator + "event.csv";

        try (Scanner sc = new Scanner(new File(eventFilePath))) {

            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String[] e = sc.nextLine().split(",");

                if (condition.check(e)) {
                    printEvent(e[1], e[2], e[3], e[4]);
                    found = true;
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading event.csv");
        }

        // ===== RECURRING EVENTS (IN MEMORY) =====
        for (int i = 0; i < recurringCount; i++) {

            String title = recurringEvents[i][0].toString();
            String desc  = recurringEvents[i][1].toString();
            String start = recurringEvents[i][2].toString();
            String end   = recurringEvents[i][3].toString();

            String[] fakeEvent = { "", title, desc, start, end };

            if (condition.check(fakeEvent)) {
                printEvent(title, desc, start, end);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching events found.");
        }
    }

    // ===== PRINT EVENT =====
    private static void printEvent(String title, String desc,
                                   String start, String end) {

        System.out.println(
                title + ", " +
                desc + ", " +
                start + ", " +
                end
        );
    }

    // ===== SIMPLE CONDITION INTERFACE =====
    private interface EventCondition {
        boolean check(String[] event);
    }
}

