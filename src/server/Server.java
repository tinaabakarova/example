package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ilnaz-92@yandex.ru
 * Created on 2019-03-28
 */
public class Server {

    public static void main(String[] args) {
        try {
            int port = 6666;
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("************************");
            System.out.println("I am waiting a client");
            System.out.println("************************");


            Socket clientSocket = serverSocket.accept();
            System.out.println("************************");
            System.out.println("I got new client");
            System.out.println("************************");

            OutputStream os = clientSocket.getOutputStream();
            InputStream is = clientSocket.getInputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(os);
            DataInputStream dataInputStream = new DataInputStream(is);


            Thread getMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line = null;
                    System.out.println("I am waiting info from client...");
                    try {
                        while (true) {
                            line = dataInputStream.readUTF();


                            if (line.equalsIgnoreCase("EXIT")) {
                                System.out.println("Client want to exit");
                                break;
                            }

                            System.out.println("Client send a message with text = " + line);

                            dataOutputStream.writeUTF("I received your message. Your message = " + line);
                            dataOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread sendMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

                            String msg = keyboard.readLine();
                            if (msg.equalsIgnoreCase("EXIT")) {
                                System.out.println("server want to exit");
                                break;
                            }

                            System.out.println("We are sending to client message = " + msg);
                            dataOutputStream.writeUTF(msg);
                            dataOutputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            getMsg.start();
            sendMsg.start();
            getMsg.join();
            sendMsg.join();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
