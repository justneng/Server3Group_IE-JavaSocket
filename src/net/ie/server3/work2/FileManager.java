/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wanchana
 */
public class FileManager {
            
    public static void sendFile(File file, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws FileNotFoundException, IOException {
        byte[] bytesBuffer = new byte[1024];
        dataInputStream = new DataInputStream(new FileInputStream(file));
        dataOutputStream.writeBytes(file.getAbsolutePath() + "#\n");
        int len = 0;
        while ((len = dataInputStream.read(bytesBuffer)) != -1) {
            dataOutputStream.write(bytesBuffer, 0, len);
            dataOutputStream.flush();
        }
        
        System.out.println("upload file " + file.getName() + " Successfull");
    }
    
    public static void recieveFile(File file, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try {
            byte[] byteBuff = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while ((len = dataInputStream.read(byteBuff)) != 4) {
                fileOutputStream.write(byteBuff, 0, len);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            System.out.println(file.getAbsolutePath() + " was downloaded");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String findFileName(String path) {
        String fileName = "";
        File file = new File(path);
        fileName = (file.getName()).substring(0, (file.getName()).indexOf("."));
        return fileName;
    }

    public static String findFileType(String path) {
        String fileType = "";
        String[] seperate = path.split("\"");
        fileType = seperate[seperate.length - 1].substring(seperate[seperate.length - 1].indexOf(".") + 1);
        return fileType;
    }
}
