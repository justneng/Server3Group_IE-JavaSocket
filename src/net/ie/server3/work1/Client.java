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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public final static int SOCKET_PORT = 13267;      // you may change this
    public final static String SERVER = "localhost";  // localhost
    // different name because i don't want to
    // overwrite the one used by server...

    public static void sendFile(File file, OutputStream outputStream) {
        try {
            byte[] bytesBuffer = new byte[1024];
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);
            FileInputStream fileInputStream = new FileInputStream(file);
            int len = 0;
            int countBytes = 0;
            while ((len = fileInputStream.read(bytesBuffer, 0, 1024)) != -1) {
                bufferedOutputStream.write(bytesBuffer, 0, len);
                bufferedOutputStream.flush();
                countBytes = countBytes + len;
                System.out.println(len + " Bytes / " + countBytes + " Bytes");
            }
            System.out.println("upload file " + file.getName() + " Successfull " + countBytes + " Bytes");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        socket = new Socket(SERVER, SOCKET_PORT);
        System.out.println("Connecting...");
        System.out.println("Please input 'sw' change to upload file.");
        try {
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
                System.out.print("Send :> ");
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                messageIn = bufferedReader.readLine();

                if (messageIn.equalsIgnoreCase("sw")) {
                    System.out.print("Upload file to client (path) : ");
                    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    String fileUpload = bufferedReader.readLine();
                    File file = new File(fileUpload);
                    if (file.exists()) {
                        printWriter.println("fine#" + file.length() + "#" + file.getName());
                        sendFile(file, outputStream);
                    } else {
                        System.out.println("File does not exist!");
                    }
                } else {
                    printWriter.println(messageIn);
                }
            } while (!messageIn.equals("bye"));
        } finally {

            if (socket != null) {
                socket.close();
            }
        }
    }

}
