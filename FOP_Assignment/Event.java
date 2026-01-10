package FOP_Assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event{

    private int eventId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private static final String EVENT_FILE = "event.csv";

    // Constructor
    public Event(int eventId, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    //Add new data to CSV File
    public void addingNewFields(){
        try{
            PrintWriter outputStream = new PrintWriter(new FileWriter(EVENT_FILE, true));
          
            outputStream.println("");
            outputStream.print(eventId + "," + title + "," + description + "," + startDateTime + "," + endDateTime);

            outputStream.close();
        }catch(IOException e){
            System.out.println("Error in output.");
        }
    }

    //Conflict Method
    public boolean hasConflict(Event other) {
        return startDateTime.isBefore(other.endDateTime) && endDateTime.isAfter(other.startDateTime);
    }


    // Accessor Method
    public int getEventId(){
        return eventId;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public LocalDateTime getStartDateTime(){
        return startDateTime;
    }

    public LocalDateTime getEndDateTime(){
        return endDateTime;
    }


    // Mutator Methed
    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setStartDateTime(LocalDateTime startDateTime){
        this.startDateTime = startDateTime;
    }

    public void setEnd(LocalDateTime endDateTime){
        this.endDateTime = endDateTime;
    }
}