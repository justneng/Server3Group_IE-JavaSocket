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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public final static int SOCKET_PORT = 13267;  // you may change this

    public static void recieveFile(File file, InputStream inputStream, int fileRecieved) throws FileNotFoundException, IOException {
        byte[] byteBuff = new byte[1024];
        int len = 0;
        int countBytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
        while (true) {
            len = bufferedInputStream.read(byteBuff, 0, 1024);
            System.out.print(len);
            fileOutputStream.write(byteBuff, 0, len);
            fileOutputStream.flush();
            countBytes = countBytes + len;
            System.out.println(" Bytes /" + countBytes + " Bytes");
            if (fileRecieved == countBytes) {
                fileOutputStream.close();
                break;
            }

        }
        System.out.println(file.getAbsolutePath() + " was downloaded " + countBytes + " Bytes");
    }

    public static void main(String[] args) throws IOException {

        ServerSocket servsock = null;
        Socket socket = null;
        BufferedReader reseived = null;
        DataOutputStream send = null;
        String input = "", output = "";
        DataInputStream inputFile = null;

        try {
            servsock = new ServerSocket(SOCKET_PORT);
            System.out.println("Waiting...");
            socket = servsock.accept();
            System.out.println("Accepted connection : " + socket);

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

                        File file = new File("c:\\Download-from-client\\recieved-" + splitMsg[2]);
                        recieveFile(file, inputStream, Integer.parseInt(splitMsg[1]));
                        System.out.println("Download file successful");
                    }
                } else {
                    System.out.println("Client Say : " + messageIn);
                }
            }

        } finally {
            if (servsock != null) {
                servsock.close();
            }
        }
    }

}
