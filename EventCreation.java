package test.java;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import test.java.FolderCreation;

public class EventCreation {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        while (true) {
            Event e = new Event();
            e.saveEvent();

            System.out.println("Create another event? (y/n)");
            if (!sc.nextLine().equalsIgnoreCase("y")) break;
            sc.close();
        }
    }
}

class Event{

    private static Scanner sc  = new Scanner(System.in);

    private static int idCounter = Event.getLastEventId();

    private int eventId;
    String title ;
    String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Event(){
        this.eventId = ++idCounter;

        boolean dateCheck = false;
        while(!dateCheck){

            while (true) {
                System.out.println("Enter Title of Event:");
                this.title = sc.nextLine().trim();
                if (!this.title.isEmpty()) break;
                    System.out.println("Title cannot be empty.\n");
                
            }

            while (true) {
                System.out.println("Enter Description of Event:");
                this.description = sc.nextLine();
                if (!this.description.isEmpty()) break;
                    System.out.println("Description cannot be empty.\n");
            }

            LocalDate startDate;
            System.out.println("\t\t   Start Date:");
            System.out.println("--------------------------------------------------------");
            while (true) {
                try {
                    System.out.println("Enter Start Date of Event (dd/MM/yyyy):");
                    String input = sc.nextLine();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
                    startDate = LocalDate.parse(input, df);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Try again.\n");
                }
            }


            LocalTime startTime;
            System.out.println("\t\t   Start Time:");
            System.out.println("--------------------------------------------------------");
            while (true) {
                try {
                    System.out.println("Enter Start Time of Event (HH:mm):");
                    String input = sc.nextLine();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("H:mm");
                    startTime= LocalTime.parse(input, df);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format. Try again.\n");
                }
            }

            LocalDate endDate;
            System.out.println("\t\t   End Date:");
            System.out.println("--------------------------------------------------------");
            while (true) {
                try {
                    System.out.println("Enter End Date of Event (dd/MM/yyyy):");
                    String input = sc.nextLine();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
                    endDate = LocalDate.parse(input, df);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Try again.\n");
                }
            }

            System.out.println("\t\t   End Time:");
            System.out.println("--------------------------------------------------------");            
            LocalTime endTime;
            while (true) {
                try {
                    System.out.println("Enter End Time of Event (HH:mm):");
                    String input = sc.nextLine();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("H:mm");
                    endTime= LocalTime.parse(input, df);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format. Try again.\n");
                }
            }

            this.startDateTime = LocalDateTime.of(startDate, startTime);
            this.endDateTime = LocalDateTime.of(endDate,endTime);

            if(!validStartEnd(this.startDateTime, this.endDateTime)){
                System.out.println("Error: End Date/Time must be after Start Date/Time.\n Please re-enter the details.\n");
            }else{
                dateCheck=true;
            }
        }
    }

    public void saveEvent(){
        String folderPath = "data";
        String filePath = folderPath+File.separator+"events.csv";

        FolderCreation.createFolder();

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedStartDateTime = this.startDateTime.format(formatter);
            String formattedEndDateTime = this.endDateTime.format(formatter);

            PrintWriter pw = new PrintWriter(new FileOutputStream(filePath,true));
            pw.printf("%d ,%s ,%s , %s , %s\n",eventId,title,description,formattedStartDateTime,formattedEndDateTime);
            System.out.println("Event Saved Successfully\n");
            pw.close();

        }catch(FileNotFoundException e){
            System.out.println("File Not Found for Saving Event");
        }catch(IOException e){
            System.out.println("IO Exception");
        }catch(Exception e){
            System.out.println("Some Error");
        }

    }

    public static int getLastEventId() { /*To prevent the eventId to reset to 1 every run */
        String filePath = "data" + File.separator + "events.csv";
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return 0;
        }

        int lastEventId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String lastLine = "";

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lastLine = line;
                }
            }

            if (!lastLine.isEmpty()) {
                String[] parts = lastLine.split(",");
                lastEventId = Integer.parseInt(parts[0].trim());
            }

        } catch (IOException e) {
            System.out.println("Error reading event file");
        }

        return lastEventId;
    }


    public static boolean validStartEnd(LocalDateTime start, LocalDateTime end){
        if (end.isEqual(start) || end.isBefore(start)){
            return false;
        }   
        return true;
    }
}