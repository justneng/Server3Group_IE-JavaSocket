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
                            printWriter.println("fine#" + file.length() + "#" + file.getName());
                            FileManager.sendFile(file, outputStream);
                        } 
                        else {
                            System.out.println("File does not exist!");
                        }
                    } 
                    else {
                        printWriter.println("Server says : " + messageIn);
                    }
                } while (!messageIn.equals("bye"));
            } 
//            Recieve Data from client
            else {
                BufferedReader bufferedReader = null;
                InputStream inputStream = null;
                String messageIn = "";
                try {
                    do {
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        messageIn = bufferedReader.readLine();
                        inputStream = socket.getInputStream();
                        
                        if (!messageIn.contains("fine#")) {
                            System.out.println(messageIn);
                        } 
                        else {
                            String[] splitMsg = messageIn.split("#");
                            System.out.println(splitMsg[0] + " " + splitMsg[1] + " " + splitMsg[2]);
                            
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

                            File file = new File("c:\\Download-from-client\\recieved-" + splitMsg[2]);
                            FileManager.recieveFile(file, inputStream, Integer.parseInt(splitMsg[1]));
                            System.out.println("Download file successful");
                        }
                    } while (!messageIn.equals("bye"));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server3();
    }
}
