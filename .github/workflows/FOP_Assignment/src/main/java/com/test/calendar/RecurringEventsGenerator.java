/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.calendar;

/**
 *
 * @author User
 */
import java.io.*;
import java.time.*;
import java.util.Scanner;

public class RecurringEventsGenerator {
    //events is for recurring ones
    Object [][] events = new Object[200][4];
    //count is for calculate how many events 
    int count = 0;
    
    public Object [][] generateRecurringEvents(){
        try{
            String folderPath = System.getProperty("user.dir") + File.separator + "data"+File.separator;

            Scanner readR = new Scanner (new FileInputStream(folderPath+"recurrent.csv"));
            readR.nextLine(); //Skip header
            
            while (readR.hasNextLine()){
                String [] recurrent = readR.nextLine().split(",");
                
                int eventId = Integer.parseInt(recurrent[0].trim());
                String interval = recurrent[1].trim();
                int times = Integer.parseInt(recurrent[2].trim());
                String endDateString = recurrent[3].trim();
                
                
                Scanner readE = new Scanner (new FileInputStream(folderPath+"event.csv"));
                if (readE.hasNextLine()) {
                    readE.nextLine(); 
                }
                
                while (readE.hasNextLine()){
                    //event is for base event ones
                    String [] event = readE.nextLine().split(",");
                    if (Integer.parseInt(event[0].trim()) == eventId){
                        String title = event[1].trim();
                        String desc = event[2].trim();
                        LocalDateTime start = LocalDateTime.parse(event[3].trim());
                        LocalDateTime end = LocalDateTime.parse(event[4].trim());
                        
                        //step is like 1 while unit is like day
                        int step = Integer.parseInt(interval.substring(0, interval.length() - 1));
                        char unit = interval.charAt(interval.length() - 1);
                        
                        if (times > 0){
                            start = move(start, step, unit);
                            end = move(end, step, unit);

                            for (int i = 0; i < times; i++) {
                                addEvent(title, desc, start, end);
                                start = move(start, step, unit);
                                end = move(end, step, unit);
                            }
                        }
                        else{
                            //Parse to readable date
                            LocalDate endDate = LocalDate.parse(endDateString);
                            
                            //Condition to check if designated date is after end date
                            start = move(start, step, unit);
                            end = move(end, step, unit);

                            while (!start.toLocalDate().isAfter(endDate)) {
                                addEvent(title, desc, start, end);
                                start = move(start, step, unit);
                                end = move(end, step, unit);
                            }
                        }
                    }
                }
                
            readE.close();
            }
            
            readR.close();
        } catch (IOException e){
        
        }
        return events;
    }
    
    public int getCount(){
        return count;
    }
    
    private void addEvent(String title, String desc, LocalDateTime start, LocalDateTime end) {
        events[count][0] = title;
        events[count][1] = desc;
        events[count][2] = start;
        events[count][3] = end;
        count++;
    }
    
    private LocalDateTime move(LocalDateTime date, int step, char unit) {
        if (unit == 'd') return date.plusDays(step);
        if (unit == 'w') return date.plusWeeks(step);
        return date;
    }
}

