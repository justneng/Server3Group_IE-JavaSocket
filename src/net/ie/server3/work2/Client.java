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
                        } else {
                            System.out.println("File does not exist!");
                        }
                    } else {
                        printWriter.println("Server says : " + messageIn);
                    }
                } while (!messageIn.equals("bye"));
            } else {
                InputStream inputStream = null;
                DataInputStream dataInputStream = null;
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = null;

                while (true) {
                    inputStream = socket.getInputStream();
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    printWriter = new PrintWriter(socket.getOutputStream(), true);

                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String messageIn = bufferedReader.readLine();

                    if (messageIn.contains("fine#")) {
                        String[] splitMsg = messageIn.split("#");
                        System.out.println(splitMsg[0] + " " + splitMsg[1] + " " + splitMsg[2]);
                        if (splitMsg[0].equals("fine")) {
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

                            File file = new File("c:\\Download-from-server\\recieved-" + splitMsg[2]);
                            FileManager.recieveFile(file, inputStream, Integer.parseInt(splitMsg[1]));
                            System.out.println("Download file successful");
                        }
                    }
                    else{
                        System.out.println(messageIn);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Client();
    }
}
