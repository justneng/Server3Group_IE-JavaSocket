/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import net.ie.server3.work3.Styles;

/**
 *
 * @author wanchana
 */
public class FileManager {

    public static void sendFile(File file, OutputStream outputStream) {
        
        try {
            byte[] bytesBuffer = new byte[10240];
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 10240);
            FileInputStream fileInputStream = new FileInputStream(file);
            int len = 0;
            int countBytes = 0;
            while ((len = fileInputStream.read(bytesBuffer, 0, 10240)) != -1) {
                bufferedOutputStream.write(bytesBuffer, 0, len);
                bufferedOutputStream.flush();
                countBytes = countBytes + len;
                System.out.println(len + " " + countBytes);
            }
            System.out.println("upload file " + file.getName() + " Successfull " + countBytes);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void recieveFile(File file, InputStream inputStream, int fileRecieved) throws FileNotFoundException, IOException {
        byte[] byteBuff = new byte[10240];
        int len = 0;
        int countBytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 10240);
        while (true) {
            len = bufferedInputStream.read(byteBuff, 0, 10240);
            System.out.println(len);
            fileOutputStream.write(byteBuff, 0, len);
            fileOutputStream.flush();
            countBytes = countBytes + len;
            System.out.println(countBytes);
            if (fileRecieved == countBytes) {
                fileOutputStream.close();
                break;
            }

        }
        System.out.println(file.getAbsolutePath() + " was downloaded " + countBytes);
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

    public static void copyFileAndDelete(File fileCopy, File fileDestination) {
        try {
            InputStream in = new FileInputStream(fileCopy);
            OutputStream out = new FileOutputStream(fileDestination);
            byte[] buffer = new byte[4048];
            int len = 0;
            while ((len = in.read(buffer)) > 0){
    	    	out.write(buffer, 0, len);
    	    }
            in.close();
            out.close();
            fileCopy.delete();
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
}
