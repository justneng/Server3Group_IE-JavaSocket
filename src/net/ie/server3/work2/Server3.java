/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work2;

import java.io.*;
import java.net.*;

/**
 *
 * @author wanchana
 */
public class Server3 implements Runnable {
    
    public static ServerSocket serversocket;
    public static Socket socket;
    public static Thread thread1;
    public static Thread thread2;
    
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
    
    public void run() {
        try {
            if (Thread.currentThread() == thread1) {
                DataInputStream dataInputStream = null;
                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = null;
                String messageIn = "";
                String messageOut = "";

                OutputStream outputStream = null;
                
                do {
                    outputStream = socket.getOutputStream();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    messageIn = bufferedReader.readLine();
        
                    if (messageIn.equalsIgnoreCase("sw")) {
                        System.out.print("Upload file to client (path) : ");
                        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                        String fileUpload = bufferedReader.readLine();
                        File file = new File(fileUpload);
                        if (file.exists()) {
                            printWriter.println("fine#"+ file.length() + "#" + file.getName());
                            FileManager.sendFile(file, outputStream);
                        } 
                        else {
                            System.out.println("File does not exist!");
                        }
                    }
                } while (!messageIn.equals("bye"));
            } 
//            else {
//                DataInputStream dataInputStream = null;
//                DataOutputStream dataOutputStream = null;
//                BufferedReader bufferedReader = null;
//                PrintWriter printWriter = null;
//                String messageIn = "";
//                String messageOut = "";
//                do {
//                    dataInputStream = new DataInputStream(socket.getInputStream());
//                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    String msg = bufferedReader.readLine();
//                    String checkMsg = msg.substring(0, 2);
//                    String fileNameRecieved = "";
//
//                    if (checkMsg.equals("m$")) {
//                        System.out.println("");
//                        messageOut = msg.substring(2, msg.length());
//                        System.out.println("Client says : : : " + messageOut);
//                    } else {
//                        fileNameRecieved = msg.substring(0, msg.indexOf("#"));
//                        File dir = new File("C:\\Download-from-client");
//                        if (!dir.exists()) {
//                            try {
//                                System.out.println("Creating... directory C:\\Download-from-client");
//                                dir.mkdir();
//                                System.out.println("The directory created");
//                            } catch (SecurityException securityException) {
//                                System.out.println("SecurityException occure!!!");
//                                securityException.printStackTrace();
//                            }
//                        }
//                        File file = new File("C:\\Download-from-client\\" + FileManager.findFileName(fileNameRecieved) + "-downloaded." + FileManager.findFileType(fileNameRecieved));
////                        FileManager.recieveFile(file, dataInputStream, dataOutputStream);
//                    }
//                } while (!messageOut.equals("bye"));
//            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server3();
    }
}
