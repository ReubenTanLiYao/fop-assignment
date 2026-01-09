package service;

import model.Event;
import util.CsvEventUtil;
import java.util.List;

public class EventService {

    public static boolean updateEvent(int eventId, Event updatedEvent) {
        List<Event> events = CsvEventUtil.readEvents();

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == eventId) {
                events.set(i, updatedEvent);
                CsvEventUtil.writeEvents(events);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteEvent(int eventId) {
        List<Event> events = CsvEventUtil.readEvents();

        boolean removed = events.removeIf(e -> e.getEventId() == eventId);
        if (removed) {
            CsvEventUtil.writeEvents(events);
        }
        return removed;
    }
}
