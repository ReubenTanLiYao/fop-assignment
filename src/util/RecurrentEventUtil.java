package util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecurrentEventUtil {

    private static final String RECURRENT_FILE = "recurrent.csv";

    public static List<String[]> readRecurrentEvents() {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(RECURRENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("*")) continue;
                records.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error reading recurrent.csv");
        }

        return records;
    }

    public static void writeRecurrentEvents(List<String[]> records) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RECURRENT_FILE))) {
            pw.println("eventId,recurrentInterval,recurrentTimes,recurrentEndDate");
            for (String[] r : records) {
                pw.println(String.join(",", r));
            }
        } catch (IOException e) {
            System.out.println("Error writing recurrent.csv");
        }
    }

    public static void deleteByEventId(int eventId) {
        List<String[]> records = readRecurrentEvents();
        Iterator<String[]> iterator = records.iterator();

        while (iterator.hasNext()) {
            if (Integer.parseInt(iterator.next()[0]) == eventId) {
                iterator.remove();
            }
        }
        writeRecurrentEvents(records);
    }

    public static void updateRecurrentEvent(
            int eventId,
            String interval,
            String times,
            String endDate
    ) {
        List<String[]> records = readRecurrentEvents();

        for (String[] r : records) {
            if (Integer.parseInt(r[0]) == eventId) {
                r[1] = interval;
                r[2] = times;
                r[3] = endDate;
            }
        }
        writeRecurrentEvents(records);
    }
}
