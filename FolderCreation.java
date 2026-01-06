import java.io.*;

public class FolderCreation{
    public static void createFolder(){
        try{
            String folderPath = "data";
            String filePath = folderPath+File.separator+"events.csv";

            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdir();
            }

        } catch(Exception e){
            System.out.println("An error occurred while creating the folder:"+ e.getMessage());
        }
    }
}
