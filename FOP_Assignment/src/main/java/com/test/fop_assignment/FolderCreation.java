/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.test.fop_assignment;

/**
 *
 * @author User
 */
import java.io.*;

public class FolderCreation{
    public static void createFolder(){
        try{
            String folderPath = "data";
            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdir();
            }
            /*System.out.println(folder.getAbsolutePath()); , to get the folder path*/
            

        } catch(Exception e){
            System.out.println("An error occurred while creating the folder:"+ e.getMessage());
        }
    }
}
