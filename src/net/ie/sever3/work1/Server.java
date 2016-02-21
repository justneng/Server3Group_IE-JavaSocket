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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public final static int SOCKET_PORT = 13267;  // you may change this

    public static String findName(String fileName) {
        String name = "";
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (c == '/') {
                name = "";
                continue;
            }
            name += c;
        }
        return name;
    }
    public static String reseivedFile(String fileName, DataOutputStream saveFile, DataInputStream inputFile) {
        File file = new File(fileName);
        try {
            saveFile = new DataOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = inputFile.read(buffer)) != 1) {
                saveFile.write(buffer, 0, len);
            }
            return "Saveded Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Save Faile";
        }
    }
    
    public static void main(String[] args) throws IOException {

        ServerSocket servsock = null;
        Socket sock = null;
        BufferedReader reseived = null;
        DataOutputStream send = null;
        String input = "", output = "";
        DataInputStream inputFile = null;

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            System.out.println("Waiting...");
            sock = servsock.accept();
            System.out.println("Accepted connection : " + sock);

            while (true) {
                    reseived = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    output = reseived.readLine();
                    if ((output.charAt(0) == 'f' || output.charAt(0) == 'F') && output.length() == 1) {
                        String fileName = reseived.readLine();
                        System.out.println(fileName);
                        fileName = "/Users/engineer/Desktop/tmpFile/" + findName(fileName);
                        System.out.println("File Upload From client : " + fileName);
                        System.out.println(reseivedFile(fileName, send, inputFile));

                    } else {
                        System.out.println("Client : " + output);
                    }
                }
            
        } finally {
            if (servsock != null) {
                servsock.close();
            }
        }
    }

   
}
