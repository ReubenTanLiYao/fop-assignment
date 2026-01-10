package service;

import model.Event;
import util.CsvEventUtil;
import util.RecurrentEventUtil;

import java.util.Iterator;
import java.util.List;

public class EventService {

    // UPDATE base event + recurrent.csv
    public static boolean updateEvent(
            int eventId,
            Event updatedEvent,
            String recurrentInterval,
            String recurrentTimes,
            String recurrentEndDate
    ) {
        List<Event> events = CsvEventUtil.readEvents();
        boolean updated = false;

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == eventId) {
                events.set(i, updatedEvent);
                updated = true;
                break;
            }
        }

        if (updated) {
            CsvEventUtil.writeEvents(events);

            // update recurrent.csv
            RecurrentEventUtil.updateRecurrent(
                    eventId,
                    recurrentInterval,
                    recurrentTimes,
                    recurrentEndDate
            );
        }

        return updated;
    }

    // DELETE base event + recurrent.csv
    public static boolean deleteEvent(int eventId) {
        List<Event> events = CsvEventUtil.readEvents();
        boolean removed = false;

        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getEventId() == eventId) {
                iterator.remove();
                removed = true;
            }
        }

        if (removed) {
            CsvEventUtil.writeEvents(events);

            // delete from recurrent.csv
            RecurrentEventUtil.deleteByEventId(eventId);
        }

        return removed;
    }
}
