/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ie.sever3.work1;

/**
 *
 * @author kasem
 */

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public final static String SERVER = "192.168.10.233";  // localhost
    public final static String FILE_TO_RECEIVED = "c:/temp/source-downloaded.pdf";  // you may change this, I give a
    // different name because i don't want to
    // overwrite the one used by server...

    public final static int FILE_SIZE = 6022386; // file size temporary hard coded
    // should bigger than the file to be downloaded

    public static String sendFile(String fileName, DataInputStream inputFile, DataOutput send) {
        File file = new File(fileName);
        try {
            inputFile = new DataInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputFile.read(buffer)) != -1) {
                send.write(buffer, 0, len);
            }
            return "Send Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Send Faile";
        }
    }

    public static void main(String[] args) throws IOException {
        int bytesRead;
        int current = 0;

        Socket sock = null;
        try {
            DataOutputStream send = null;
            String input = "", output = "";
            DataInputStream inputFile = null;

            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");
            System.out.println("Please input 'sw' change to upload file.");

            // send file
             while (true) {
                    send = new DataOutputStream(sock.getOutputStream());
                    Scanner scanner = new Scanner(System.in);
                    input = scanner.nextLine();
                    send.writeBytes(input + "\n");
                    if (input.equalsIgnoreCase("f")) {
                        System.out.print("Upload file : ");
                        String fileName = scanner.nextLine();
                        send.writeBytes(fileName + "\n");
                        System.out.println( sendFile(fileName, inputFile, send));
                        continue;
                    }
                }
        } finally {

            if (sock != null) {
                sock.close();
            }
        }
    }

}
