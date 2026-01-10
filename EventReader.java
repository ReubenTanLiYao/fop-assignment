package test.java;

import java.io.*;
import java.time.LocalDateTime;

import FOP_Assignment.RecurringEventsGenerator;


public class EventReader{

        public static String[][] readEvents(int year, int month){
            String folderPath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
            File fileE = new File(folderPath+"event.csv");
            File fileR = new File(folderPath+"recurrent.csv");

            String [][] eventDays = new String[100][3];
            for(int i=0;i<32;i++){
                for(int j=0;j<2;j++){
                    eventDays[i][j] = "0";
                }
            }

            if(!fileE.exists()||!fileR.exists()){
                return eventDays;
            }

        try{

            BufferedReader brE = new BufferedReader(new FileReader(fileE));
            BufferedReader brR = new BufferedReader(new FileReader(fileR));

            /*For Normal Events */
            String line;
            int counter=0;
            while((line=brE.readLine())!=null){
                if (line.trim().isEmpty()) continue;

                String parts[] = line.split(",");

                LocalDateTime start = LocalDateTime.parse(parts[3].trim());
                
                if(start.getYear()==year && start.getMonthValue()==month){
                    int day = start.getDayOfMonth();
                    eventDays[day][0] = "1";
                    eventDays[day][1] = parts[1].trim();
                    eventDays[day][2] = parts[3].trim();
                }
                else{
                    int day = start.getDayOfMonth();
                    eventDays[day][0] = "0";
                }
            }   

            /*For Recurring Events */
            RecurringEventsGenerator gen = new RecurringEventsGenerator();
            Object[][] recurring = gen.generateRecurringEvents();
            int total = gen.getCount();

            for (int i = 0; i < total; i++) {
                LocalDateTime start = LocalDateTime.parse(String.valueOf(recurring[i][2]));

                if (start.getYear() == year && start.getMonthValue() == month) {
                    int day = start.getDayOfMonth();
                    eventDays[day][0] = "1";
                    eventDays[day][1] = String.valueOf(recurring[i][0]); // title
                    eventDays[day][2] = String.valueOf(recurring[i][2]); // start time
                }
            }

            brE.close();
            brR.close();
        }catch(FileNotFoundException e){
            System.out.println("File not Found while reading Event List.");
        }catch(Exception e){
            System.out.println("Some Error Occurred while reading Event List");
        }
        return eventDays;
    }

    public static void eventDetails(int year,int month){
            String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "event.csv";
            File file = new File(filePath);
        try{
            BufferedReader brE = new BufferedReader(new FileReader(filePath));
            
        }catch(FileNotFoundException e){
            System.out.println("File Not Found while reading Event Details.");
        }catch(Exception e){
            System.out.println("Some Error Occurred:" +e.getMessage());
        }
    }
}