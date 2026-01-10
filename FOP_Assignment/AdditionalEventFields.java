package FOP_Assignment;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AdditionalEventFields{ 

    private int eventId;
    private String field1;
    private String field2;
    private String field3;

    private static final String ADDITIONAL_FILE = "additional.csv";
    private static final String BACKUP_FILE = "additional_backup.csv";
    
    // Constructor
    public AdditionalEventFields(int eventId, String field1, String field2, String field3){ 
        this.eventId = eventId;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    //Add new data to CSV File
    public void addingNewFields(){
        try{
            PrintWriter outputStream = new PrintWriter(new FileWriter(ADDITIONAL_FILE, true));
          
            outputStream.println("");
            outputStream.print(eventId + "," + field1 + "," + field2 + "," + field3);

            outputStream.close();
        }catch(IOException e){
            System.out.println("Error in output.");
        }
    }


    // Search additional fields by eventId
    public void searchByEventId(int ID){
        try{
            BufferedReader buffer = new BufferedReader(new FileReader(ADDITIONAL_FILE));
            String line;
            boolean found = false;

            // Skip header line
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                int eventId = Integer.parseInt(data[0]);

                if(eventId == ID){
                    System.out.println("Event ID: " + data[0]);
                    System.out.println("Field 1: " + data[1]);
                    System.out.println("Field 2: " + data[2]);
                    System.out.println("Field 3: " + data[3]);
                    found = true;
                    break;
                }
            }
            System.out.println();
            System.out.println("Event found: " + found);

            buffer.close();
        }catch(IOException e){
            System.out.println("Problem with file");
        }
    }

    //Backup Additional CSV File
    public void backup(){
        try{
            Files.copy(Paths.get(ADDITIONAL_FILE), Paths.get(BACKUP_FILE), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Backup created successfully.");
        }catch(IOException e){
            System.out.println("Error in backup");
        }finally{
            System.out.println("Backup block is executed");
        }
    }


    // Restore from backup
    public void restore(){
        try{
            Files.copy(Paths.get(BACKUP_FILE), Paths.get(ADDITIONAL_FILE), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Restoration created successfully.");
        }catch(IOException e){
            System.out.println("Error in restoration");
        }finally{
            System.out.println("Restore block is executed");
        }
    }
}