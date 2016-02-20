/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.server3.work2;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wanchana
 */
public class Client implements Runnable {

    public static Socket socket;
    public static Thread thread1;
    public static Thread thread2;

    public Client() {
        try {
            thread1 = new Thread(this);
            thread2 = new Thread(this);
//            socket = new Socket("192.168.10.233", 55555);
            socket = new Socket("localhost", 12121);
            thread1.start();
            thread2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            if (Thread.currentThread() == thread2) {
                DataInputStream dataInputStream = null;
                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = null;
                String messageIn = "";
                String messageOut = "";
                    
                do {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    messageIn = bufferedReader.readLine();

                    if (messageIn.equalsIgnoreCase("sw")) {
                        System.out.print("Upload file to server (path) : ");
                        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                        String fileUpload = bufferedReader.readLine();
                        File file = new File(fileUpload);
                        if (file.exists()) {
                            FileManager.sendFile(file, dataInputStream, dataOutputStream);
                            System.out.println("Please Enter to continue...");
                        } else {
                            System.out.println("File does not exist!");
                        }
                    } else {
                        printWriter.println("m$" + messageIn);
                    }
                } while (!messageIn.equals("bye"));
            } else {
                DataInputStream dataInputStream = null;
                DataOutputStream dataOutputStream = null;
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = null;
                String messageIn = "";
                String messageOut = "";
                do {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg = bufferedReader.readLine();
                    String checkMsg = msg.substring(0, 2);
                    String fileNameRecieved = "";

                    if (checkMsg.equals("m$")) {
                        System.out.println("");
                        messageOut = msg.substring(2, msg.length());
                        System.out.println("Server says : : : " + messageOut);
                    } else {
                        fileNameRecieved = msg.substring(0, msg.indexOf("#"));
                        File dir = new File("C:\\Download-from-server");
                        if (!dir.exists()) {
                            try {
                                System.out.println("Creating... directory C:\\Download-from-server");
                                dir.mkdir();
                                System.out.println("The directory created");
                            } catch (SecurityException securityException) {
                                System.out.println("SecurityException occure!!!");
                                securityException.printStackTrace();
                            }
                        }

                        File file = new File("C:\\Download-from-server\\" + FileManager.findFileName(fileNameRecieved) + "-downloaded." + FileManager.findFileType(fileNameRecieved));
                        FileManager.recieveFile(file, dataInputStream, dataOutputStream);
                    }

                } while (!messageOut.equals("bye"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Client();
    }
}
