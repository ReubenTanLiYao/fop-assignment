import java.io.*;

public class BackupRestoreManager {

    private static final String DATA_DIR = "data/";
    private static final String EVENT_FILE = "event.csv";
    private static final String RECURRENT_FILE = "recurrent.csv";

    /* =========================
       BACKUP
       ========================= */
    public static void backup(String backupPath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(backupPath))) {

            backupFile(bw, "EVENT", DATA_DIR + EVENT_FILE);
            backupFile(bw, "RECURRENT", DATA_DIR + RECURRENT_FILE);

            System.out.println("Backup has been completed to " + backupPath);

        } catch (IOException e) {
            System.out.println("Backup failed: " + e.getMessage());
        }
    }

    private static void backupFile(BufferedWriter bw, String tag, String filePath)
            throws IOException {

        File file = new File(filePath);
        if (!file.exists()) return;

        bw.write("#" + tag);
        bw.newLine();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
        }
        bw.newLine();
    }

    /* =========================
       RESTORE
       ========================= */
    public static void restore(String backupPath, boolean overwrite) {
        try (BufferedReader br = new BufferedReader(new FileReader(backupPath))) {

            if (overwrite) clearFiles();

            String line;
            BufferedWriter currentWriter = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("#")) {
                    if (currentWriter != null) currentWriter.close();
                    currentWriter = getWriter(line.substring(1), overwrite);
                } 
                else if (currentWriter != null && !line.trim().isEmpty()) {
                    currentWriter.write(line);
                    currentWriter.newLine();
                }
            }

            if (currentWriter != null) currentWriter.close();

            System.out.println("Restore completed successfully.");

        } catch (IOException e) {
            System.out.println("Restore failed: " + e.getMessage());
        }
    }

    private static BufferedWriter getWriter(String tag, boolean overwrite)
            throws IOException {

        switch (tag) {
            case "EVENT":
                return new BufferedWriter(
                        new FileWriter(DATA_DIR + EVENT_FILE, !overwrite));
            case "RECURRENT":
                return new BufferedWriter(
                        new FileWriter(DATA_DIR + RECURRENT_FILE, !overwrite));
            default:
                return null;
        }
    }

    private static void clearFiles() throws IOException {
        new FileWriter(DATA_DIR + EVENT_FILE, false).close();
        new FileWriter(DATA_DIR + RECURRENT_FILE, false).close();
    }
}