package test.java;
/* Declare your Own Package*/
import java.io.*;

public class FolderCreation{
    public static void createFolder(){
        try{
            String folderPath = "data";
            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdir();
            }
            /*System.out.println(folder.getAbsolutePath());, to get the folder path*/
            

        } catch(Exception e){
            System.out.println("An error occurred while creating the folder:"+ e.getMessage());
        }
    }
}
