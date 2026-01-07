package test.java;

import java.time.*;
import java.util.Scanner;
import java.io.*;

public class CalendarView {
    
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        displayEventList();
        CalenderMonthView();
    }

    public static void WeekView(){

    }
    public static void CalenderMonthView(){

        boolean isValid = false;
        while(!isValid){

        try{
            System.out.println("Enter Month/Year(e.g. 1/2026):");
            String dateInput = sc.nextLine();
            String parts[] = dateInput.split("/");

            if (dateInput.trim().isEmpty()) { 
                throw new IllegalArgumentException("Error: Input cannot be empty.\n");
            }

            for (int i = 0; i < dateInput.length(); i++) {
                char c = dateInput.charAt(i);
                if (Character.isLetter(c)) {
                    throw new IllegalArgumentException("Error: Invalid Input. Alphabets are not allowed.\n");
                }
            }

            if(!dateInput.contains("/")){
                throw new IllegalArgumentException("Error: Invalid Format. Please use the format Month/Year (e.g. 1/2026).\n");
            }
                
            if(parts.length!=2){
                throw new IllegalArgumentException("Error: Invalid format. Please use the format Month/Year (e.g. 1/2026).\n");
            }


            int year = Integer.parseInt(parts[1]);
            int monthList = Integer.parseInt(parts[0]);


            LocalDate date = LocalDate.of(year, monthList, 1);
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

            System.out.printf("\n\t     "+monthList(monthList)+" "+year+"\n");
            System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);
            System.out.println(Colors.Blue+"|"+Colors.Reset+" Su "+Colors.Blue+"|"+Colors.Reset+" Mo "+Colors.Blue+"|"+Colors.Reset+" Tu "+Colors.Blue+"|"+Colors.Reset+" We "+Colors.Blue+"|"+Colors.Reset+" Th "+Colors.Blue+"|"+Colors.Reset+" Fr "+Colors.Blue+"|"+Colors.Reset+" Sa "+Colors.Blue+"|"+Colors.Reset);
            System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);

            int daysInMonth = daysInMonth(monthList, year);
            int counter=firstDayofMonth;

            System.out.print("|");
            for (int i = 0; i < firstDayofMonth; i++) {
                System.out.print("    |");
            }

            for (int i = 1; i <= daysInMonth; i++) {
                System.out.printf(" %2d "+Colors.Blue+"|"+Colors.Reset, i);
                counter++;
                if (counter == 7) {
                    System.out.println();
                    System.out.println(Colors.Blue+"+----+----+----+----+----+----+----+"+Colors.Reset);
                    if (i != daysInMonth) {
                        System.out.print(Colors.Blue+"|"+Colors.Reset);
                    }
                    counter = 0;
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

    public static Integer daysInMonth(int monthList,int year){
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

    public static void displayEventList(){
        try{
        String filePath = "data"+File.separator+"events.csv";
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        System.out.println("\t\t   Event List:");
        System.out.println("--------------------------------------------------------");       

        while((line=br.readLine())!=null){
            System.out.println(line);
        }
        br.close();
    }catch(FileNotFoundException e){
        System.out.println("File not Found while displaying Event List.");
    }catch(Exception e){
        System.out.println("Some Error Occurred while displaying Event List");
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

