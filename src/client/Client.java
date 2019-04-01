package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        int serverPort = 6666;

        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            System.out.println("************************");
            System.out.println("I am new client");
            System.out.println("I will connect with server");
            System.out.println("************************");

            Socket socket = new Socket(ipAddress, serverPort);

            System.out.println("************************");
            System.out.println("We have a connection");
            System.out.println("************************");

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            DataInputStream dataInputStream = new DataInputStream(is);
            DataOutputStream dataOutputStream = new DataOutputStream(os);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket(ipAddress, serverPort);
                            String serverMsg = null;

                            InputStream is = socket.getInputStream();
                            OutputStream os = socket.getOutputStream();

                            DataInputStream dataInputStream = new DataInputStream(is);
                            DataOutputStream dataOutputStream = new DataOutputStream(os);

                            serverMsg = dataInputStream.readUTF();
                            dataOutputStream.writeUTF(serverMsg);
                            dataOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();

                String msg = keyboard.readLine();

                if (msg.equalsIgnoreCase("EXIT")) {
                    System.out.println("I want to exit");
                    break;
                }

                dataOutputStream.writeUTF(msg);
                dataOutputStream.flush();

                thread.join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
