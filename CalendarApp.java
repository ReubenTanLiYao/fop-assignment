import java.util.ArrayList;
import java.util.List;

import FOP_Assignment.AdditionalEventFields;
import FOP_Assignment.ConflictDetection;
import FOP_Assignment.Event;
import FOP_Assignment.RecurringEventsGenerator;

import java.time.LocalDateTime;

public class CalendarApp{
    public static void main(String[] args){

        //Additional Event Fields Test
        AdditionalEventFields event1 = new AdditionalEventFields(1, "TEST1", "TIME 1", "DJAN");
        AdditionalEventFields event2 = new AdditionalEventFields(2, "TEST2", "TIME 2", "DJANINY");

        /*event1.addingNewFields();
        event2.addingNewFields();
        event1.searchByEventId(1);
        System.out.println("");
        event2.searchByEventId(2);
        System.out.println("");
        event1.backup();
        System.out.println("");
        event1.restore();*/

        
        //Conflict Detection Test
        ConflictDetection test = new ConflictDetection();

        List<Event> events = test.readEventsFromCSV();
        test.detectConflicts(events);
    }
}

