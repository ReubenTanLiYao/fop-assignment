package test.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import test.java.EventReader;

public class CalendarView {
    
    public static Scanner sc = new Scanner(System.in);

    public static void calendarSelection(){
        System.out.println("Please Choose Type of Calendar");
        System.out.println("1 for Month \n 2 for Week \n 3 for Day");

    }

    public static void dayView() {

    if (!askYesNo("View Calendar Day?")) {
        return;
    }

    try {
        System.out.println("\nEnter date (dd-mm-yyyy):");
        String input = sc.nextLine().trim();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("d-M-yyyy");
        LocalDate selectedDate = LocalDate.parse(input, df);

        System.out.println("\n" + selectedDate.getDayOfWeek() + " " + selectedDate);
        System.out.println("----------------------------------------");

        String[] titles = new String[100];
        LocalTime[] times = new LocalTime[100];
        int count = 0;

        String filePath = System.getProperty("user.dir")
                + File.separator + "data"
                + File.separator + "event.csv";

        File file = new File(filePath);
        
        if (!file.exists()) {
            System.out.println("No events found.");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            LocalDateTime start = LocalDateTime.parse(parts[3].trim());

            if (start.toLocalDate().equals(selectedDate)) {
                titles[count] = parts[1].trim();
                times[count] = start.toLocalTime();
                count++;
            }
        }
        br.close();

        if (count == 0) {
            System.out.println("(No events for this day)");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.printf(
                "- %s (%02d:%02d) \n",
                titles[i],
                times[i].getHour(),
                times[i].getMinute()
            );
        }

    } catch (Exception e) {
        System.out.println("Invalid date format. Use dd-mm-yyyy.");
    }
}

    public static void weekView() {

        if (!askYesNo("View Calendar Week?")) {
            return;
        }

        try {
            System.out.println("\nEnter date (dd-mm-yyyy):");
            String input = sc.nextLine().trim();

            DateTimeFormatter df = DateTimeFormatter.ofPattern("d-M-yyyy");
            LocalDate inputDate = LocalDate.parse(input, df);


            LocalDate weekStart = inputDate.minusDays(inputDate.getDayOfWeek().getValue() % 7);
            LocalDate weekEnd = weekStart.plusDays(6);

            System.out.println("\nWeek: " + weekStart + " to " + weekEnd);
            System.out.println("----------------------------------------");


            String[] titles = new String[100];
            LocalDate[] dates = new LocalDate[100];
            LocalTime[] times = new LocalTime[100];
            int count = 0;

            String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "event.csv";

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("No events found.");
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                LocalDateTime start = LocalDateTime.parse(parts[3].trim());
                LocalDate eventDate = start.toLocalDate();

                if (!eventDate.isBefore(weekStart) && !eventDate.isAfter(weekEnd)) {
                    titles[count] = parts[1].trim();
                    dates[count] = eventDate;
                    times[count] = start.toLocalTime();
                    count++;
                }
            }
            br.close();


            for (int i = 0; i < 7; i++) {
                LocalDate day = weekStart.plusDays(i);
                System.out.println(day.getDayOfWeek() + " " + day);
                boolean hasEvent = false;

                for (int j = 0; j < count; j++) {
                    if (dates[j].equals(day)) {
                        System.out.printf(
                            "  - %s (%02d:%02d)\n",titles[j],times[j].getHour(), times[j].getMinute());
                        hasEvent = true;
                    }
                }

                if (!hasEvent) {
                    System.out.println("  (No events)");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Invalid date format. Use dd-mm-yyyy.");
        }
    }


    public static void monthView(){
        
        if(!askYesNo("View Calendar Month?")){
            return;
        }

        System.out.println("\n\t\t   Viewing Month:");
        System.out.println("--------------------------------------------------------");

        boolean isValid = false;
        while(!isValid){

        try{
            System.out.println("\nEnter Month/Year(e.g. 1-2026):");
            String dateInput = sc.nextLine();
            String parts[] = dateInput.split("-");

            if (dateInput.trim().isEmpty()) { 
                throw new IllegalArgumentException("Error: Input cannot be empty.\n");
            }

            for (int i = 0; i < dateInput.length(); i++) {
                char c = dateInput.charAt(i);
                if (Character.isLetter(c)) {
                    throw new IllegalArgumentException("Error: Invalid Input. Alphabets are not allowed.\n");
                }
            }

            if(!dateInput.contains("-")){
                throw new IllegalArgumentException("Error: Invalid Format. Please use the format Month/Year (e.g. 1-2026).\n");
            }
            if(parts.length!=2){
                throw new IllegalArgumentException("Error: Invalid format. Please use the format Month/Year (e.g. 1-2026).\n");
            }


            int year = Integer.parseInt(parts[1]);
            int month = Integer.parseInt(parts[0]);


            LocalDate date = LocalDate.of(year, month, 1);
            DayOfWeek firstDayofWeek = date.getDayOfWeek();
            int firstDayofMonth=0;

            if(firstDayofWeek == DayOfWeek.MONDAY){
                firstDayofMonth=1;
            }
            else if(firstDayofWeek == DayOfWeek.TUESDAY){
                firstDayofMonth=2;
            }
            else if(firstDayofWeek == DayOfWeek.WEDNESDAY){
                firstDayofMonth=3;
            }
            else if(firstDayofWeek == DayOfWeek.THURSDAY){
                firstDayofMonth=4;
            }
            else if(firstDayofWeek == DayOfWeek.FRIDAY){
                firstDayofMonth=5;
            }
            else if(firstDayofWeek == DayOfWeek.SATURDAY){
                firstDayofMonth=6;
            }
            else if(firstDayofWeek == DayOfWeek.SUNDAY){
                firstDayofMonth=0;
            }

            System.out.printf("\n\t     "+monthList(month)+" "+year+"\n");
            System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);
            System.out.println(Colors.Blue+"|"+Colors.Reset+" Su "+Colors.Blue+"|"+Colors.Reset+" Mo "+Colors.Blue+"|"+Colors.Reset+" Tu "+Colors.Blue+"|"+Colors.Reset+" We "+Colors.Blue+"|"+Colors.Reset+" Th "+Colors.Blue+"|"+Colors.Reset+" Fr "+Colors.Blue+"|"+Colors.Reset+" Sa "+Colors.Blue+"|"+Colors.Reset);
            System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);

            int daysInMonth = daysInMonth(year , month);
            int counter=firstDayofMonth;


            System.out.print("|");
            for (int i = 0; i < firstDayofMonth; i++) {
                System.out.print("    |");
            }

            Object[][] eventDays = EventReader.readEvents(year,month);
            String[] eventList = new String[32];
            int[] eventDate = new int[32];
            int[] eventTimeHour = new int[32];
            int[] eventTimeMinute = new int[32];

            for (int i = 1; i <= daysInMonth; i++) {
                if(Integer.parseInt(String.valueOf(eventDays[i][0])) == 1){
                    eventList[i] = String.valueOf(eventDays[i][1]);

                    LocalDateTime start = LocalDateTime.parse(String.valueOf(eventDays[i][2]));
                    eventDate[i] = start.getDayOfMonth();
                    eventTimeHour[i] = start.getHour();
                    eventTimeMinute[i] = start.getMinute();

                    System.out.printf(Colors.Red+" %2d*"+Colors.Blue+"|"+Colors.Reset, i);
                }else{
                    System.out.printf(" %2d "+Colors.Blue+"|"+Colors.Reset, i);
                }

               counter++;
                if (counter % 7==0) {
                    System.out.println();
                    System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);
                    if (i != daysInMonth) {
                        System.out.print(Colors.Blue+"|"+Colors.Reset);
                    }
                }
            }

        System.out.println("Events For the Month:");
        for (int i = 0; i < eventList.length; i++) {
            if (eventList[i] != null) {
                System.out.printf(
                    Colors.Red + "* %2d" + Colors.Reset + ": %-15s (%02d:%02d)%n",
                    eventDate[i],
                    eventList[i],
                    eventTimeHour[i],
                    eventTimeMinute[i]
                );  
            }
        }
            isValid=true;
        } 

        catch (java.time.DateTimeException e){
            System.out.println("Error: Invalid month. Please enter 1-12.\n");
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }
    
    public static boolean leapYear(int year){
        if (year%4==0){
            if (year%100==0){
                if (year%400==0){
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
    public static String monthList(int monthList){
        String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return monthName[monthList-1]; 
    }

    public static Integer daysInMonth(int year,int monthList){
        if (leapYear(year) && monthList==2){
            return 29;
        }else if(monthList ==1 || monthList==3 || monthList == 5 || monthList == 7 || monthList ==8 || monthList == 10 || monthList ==12){
            return 31;
        }else if (!leapYear(year) && monthList ==2){
            return 28;
        }else{
            return 30;
        }
    }

    
    public static boolean askYesNo(String message) {
        while (true) {
            System.out.println(message + " (y/n)");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("y")) return true;
            if (input.equalsIgnoreCase("n")) return false;

            System.out.println("Invalid input. Please enter y or n.\n");
        }
    }

}

public class Colors{
    public static final String Reset = "\u001B[0m";
    public static final String Red   = "\u001B[31m";
    public static final String Green = "\u001B[32m";
    public static final String Blue  = "\u001B[34m";
    public static final String CyanN  = "\u001B[36m";
    public static final String Yellow = "\u001B[33m";
    public static final String White = "\u001B[37m";
}

