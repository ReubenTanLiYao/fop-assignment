package FOP_Assignment;

import java.io.*;
import java.util.Scanner;

public class BackupRestore {

    private static final String DATA_FOLDER = "data";
    private static final String BACKUP_FILE = "backup.txt";

    private static final String EVENT_FILE = "event.csv";
    private static final String RECURRENT_FILE = "recurrent.csv";
    private static final String ADDITIONAL_FILE = "additional.csv";

    // ===== BACKUP =====
    public static void backup() {

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(BACKUP_FILE))) {

            pw.println("#EVENTS");
            copyFileToWriter(DATA_FOLDER + File.separator + EVENT_FILE, pw);

            pw.println("\n#RECURRENT");
            copyFileToWriter(DATA_FOLDER + File.separator + RECURRENT_FILE, pw);

            pw.println("\n#ADDITIONAL");
            copyFileToWriter(DATA_FOLDER + File.separator + ADDITIONAL_FILE, pw);

            System.out.println("Backup has been completed to " + BACKUP_FILE);

        } catch (Exception e) {
            System.out.println("Backup failed.");
        }
    }

    // ===== RESTORE WITH USER OPTION =====
    public static void restore() {

        Scanner input = new Scanner(System.in);

        System.out.println("\nRestore Options:");
        System.out.println("1. Append to existing data");
        System.out.println("2. Delete all and restore from backup");
        System.out.print("Choose option: ");

        String choice = input.nextLine();

        boolean overwrite = choice.equals("2");

        File eventFile = new File(DATA_FOLDER + File.separator + EVENT_FILE);
        File recurrentFile = new File(DATA_FOLDER + File.separator + RECURRENT_FILE);
        File additionalFile = new File(DATA_FOLDER + File.separator + ADDITIONAL_FILE);

        try {
            if (overwrite) {
                new PrintWriter(eventFile).close();
                new PrintWriter(recurrentFile).close();
                new PrintWriter(additionalFile).close();
            }

            Scanner sc = new Scanner(new File(BACKUP_FILE));

            PrintWriter eventWriter =
                    new PrintWriter(new FileOutputStream(eventFile, true));
            PrintWriter recurrentWriter =
                    new PrintWriter(new FileOutputStream(recurrentFile, true));
            PrintWriter additionalWriter =
                    new PrintWriter(new FileOutputStream(additionalFile, true));

            PrintWriter currentWriter = null;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.equals("#EVENTS")) {
                    currentWriter = eventWriter;
                    continue;
                }

                if (line.equals("#RECURRENT")) {
                    currentWriter = recurrentWriter;
                    continue;
                }

                if (line.equals("#ADDITIONAL")) {
                    currentWriter = additionalWriter;
                    continue;
                }

                if (!line.trim().isEmpty() && currentWriter != null) {
                    currentWriter.println(line);
                }
            }

            sc.close();
            eventWriter.close();
            recurrentWriter.close();
            additionalWriter.close();

            System.out.println("Restore completed successfully.");

        } catch (Exception e) {
            System.out.println("Restore failed. Backup file may not exist.");
        }
    }

    // ===== HELPER METHOD =====
    private static void copyFileToWriter(String filePath, PrintWriter pw) {

        File file = new File(filePath);
        if (!file.exists()) return;

        try (Scanner sc = new Scanner(file)) {

            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                pw.println(sc.nextLine());
            }

        } catch (Exception e) {
            System.out.println("Error reading " + filePath);
        }
    }
}
