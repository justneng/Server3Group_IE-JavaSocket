/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;

/**
 *
 * @author wanchana
 */
public class Server3 implements Runnable {
    
    public static ServerSocket serversocket;
    public static BufferedReader bufferedReader1;
    public static BufferedReader bufferedReader2;
    public static PrintWriter printWriter;
    public static BufferedOutputStream bufferedOutputStream;
    public static OutputStream outputStream;
    public static Socket socket;
    public static Thread thread1;
    public static Thread thread2;
    public static String messageIn = "";
    public static String messageOut = "";
    public static FileInputStream fileInputStream;
    public static DataOutputStream dataOutputStream;
    public static DataInputStream dataInputStream;
    public static FileOutputStream fileOutputStream;
    public static InputStream inputStream;
    public static BufferedInputStream bufferedInputStream;
    public static BufferedReader bufferedReader ;
    
    public Server3() {
        try {
            thread1 = new Thread(this);
            thread2 = new Thread(this);
            serversocket = new ServerSocket(12121);
            System.out.println("Server is waiting. . . . ");
            socket = serversocket.accept();
            System.out.println("Client connected with Ip " + socket.getInetAddress().getHostAddress());
            System.out.println("Please input 'sw' change to upload file.");
            thread1.start();
            thread2.start();

        } catch (Exception e) {
        }
    }

    public static void sendFile(File file, String fileName) throws FileNotFoundException, IOException {
        byte[] bytesBuffer = new byte[1024];
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(new FileInputStream(file));
        dataOutputStream.writeBytes(fileName + "#\n");
        int len = 0;
        while ((len = dataInputStream.read(bytesBuffer)) != -1) {
            dataOutputStream.write(bytesBuffer, 0, len);
            dataOutputStream.flush();
        }
        System.out.println("upload file " + fileName + " Successfull");
    }

    public String findFileName(String path) {
        String fileName = "";
        File file = new File(path);
        fileName = (file.getName()).substring(0, (file.getName()).indexOf("."));
        return fileName;
    }

    public String findFileType(String path) {
        String fileType = "";
        String[] seperate = path.split("\"");
        fileType = seperate[seperate.length - 1].substring(seperate[seperate.length - 1].indexOf(".") + 1);
        return fileType;
    }

    public static void recieveFile(String fileName, int fileNameLenght) {
        try {
            byte[] byteBuff = new byte[1024];
            int len = -1;
            File fileRecived = new File(fileName); 
            FileOutputStream fileOutputStream = new FileOutputStream(fileRecived);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while ((len = dataInputStream.read(byteBuff)) != 4) {
                fileOutputStream.write(byteBuff, 0, len);
            }
            System.out.println(fileRecived.getAbsolutePath() + " was downloaded");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        try {
            if (Thread.currentThread() == thread1) {
                do {
                    bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    messageIn = bufferedReader1.readLine();
                    
                    if (messageIn.equalsIgnoreCase("sw")) {
                        System.out.print("Upload file to client (path) : ");
                        bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                        String fileUpload = bufferedReader1.readLine();
                        File file = new File(fileUpload);
                        if (file.exists()) {
                            sendFile(file, fileUpload);
                            System.out.println("Please Enter to continue...");
                        } else {
                            System.out.println("File does not exist!");
                        }
                    } else {
                        printWriter.println("m$" + messageIn);
                    }
                } while (!messageIn.equals("bye"));
            } 
            else {
                do {
                    bufferedReader2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg = bufferedReader2.readLine();
                    String checkMsg = msg.substring(0, 2);
                    String fileNameRecieved = "";

                    if (checkMsg.equals("m$")) {
                        System.out.println("");
                        messageOut = msg.substring(2, msg.length());
                        System.out.println("Client says : : : " + messageOut);
                    } else {
                        fileNameRecieved = msg.substring(0, msg.indexOf("#"));
                        File dir = new File("C:\\Download-from-client");
                        if (!dir.exists()) {
                            try {
                                System.out.println("Creating... directory C:\\Download-from-client");
                                dir.mkdir();
                                System.out.println("The directory created");
                            } catch (SecurityException securityException) {
                                System.out.println("SecurityException occure!!!");
                                securityException.printStackTrace();
                            }
                        }
                        String fileName = "C:\\Download-from-client\\" + findFileName(fileNameRecieved) + "-downloaded." + findFileType(fileNameRecieved);
                        recieveFile(fileName, fileNameRecieved.length() + 1);
                    }
                } while (!messageOut.equals("bye"));
            }
            
        } catch (Exception e) {
        }
    }

    public static void closeService() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (bufferedOutputStream != null) {
            bufferedOutputStream.close();
        }
    }

    public static void main(String[] args) {
        new Server3();
    }
}
