/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

/**
 *
 * @author reube
 */
import java.io.*;
import java.time.*;
import java.util.*;

public class EventStatistics{
    static String event_file = "C:\\event.csv";
    public static void main(String[] args) {
        
    }
    public static void showStats(){
        int [] dayCount = new int [7];
        int [] monthCount = new int [12];
        int totalEvents = 0;
        int totalMinutes = 0;
        try{
            Scanner read = new Scanner (new FileInputStream(event_file));

            //Skip header
            if (read.hasNextLine())
                read.nextLine();
            
            //Read file
            while (read.hasNextLine()){
                String line = read.nextLine();
                String [] parts = line.split(",");

                //Convert it to readable date and time. Like parse int converts string to integer
                LocalDateTime start = LocalDateTime.parse(parts[3]);
                LocalDateTime end = LocalDateTime.parse(parts[4]);
                
                //Calculate the duration of the event
                int startMinutes = start.getHour() * 60 + start.getMinute();
                int endMinutes = end.getHour() * 60 + end.getMinute();
                int minutes = endMinutes - startMinutes;
                totalMinutes += minutes;
                
                //Get what day and add to count
                int day = start.getDayOfWeek().getValue() - 1;
                dayCount[day]++;

                //Get what month and add to count
                int month = start.getMonthValue() - 1;
                monthCount[month]++;
                
                //Calculate total events
                totalEvents++;
            }
            read.close();
        } catch (IOException e){
            System.out.print("Error");
        }

        String [] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        String [] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int highestMonthCount = 0;
        int highestDayCount = 0;
        String busiestMonth = "";
        String busiestDay = "";
        System.out.println("===== EVENT STATISTICS =====");
        if (totalEvents > 0) {
            //long increase too many events
            long avgMinutes = totalMinutes / totalEvents;
            System.out.println("Total events: " + totalEvents);
            System.out.println("Total scheduled time: " + (totalMinutes / 60) + " hours " + (totalMinutes % 60) + " minutes");
            System.out.println("Average event duration: " + (avgMinutes / 60) + " hours " + (avgMinutes % 60) + " minutes");
            } 
        else {
            System.out.println("No events found.");
        }   
        //Print and determine the busiest day
        System.out.println("Events per day of the week:");
        for (int i = 0; i < 7; i++) {
            System.out.println(days[i] + ": " + dayCount[i] + " event(s)");
            if (dayCount[i] > highestDayCount) {
                highestDayCount = dayCount[i];
                busiestDay = days[i];
            }
        }
        System.out.println("Busiest day: " + busiestDay + " (" + highestDayCount + " events)");

        //Determine and print the busiest month
        System.out.println("\nEvents per month:");
        for (int i = 0; i < 12; i++) {
            System.out.println("Month " + (i + 1) + ": " + monthCount[i] + " event(s)");
            if (monthCount[i] > highestMonthCount) {
                highestMonthCount = monthCount[i];
                busiestMonth = months[i];
            }
        }
        System.out.println("Busiest month: " + busiestMonth + " (" + highestMonthCount + " events)");
    }
}

