package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String ADDRESS = "localhost";
    private final int PORT = 8189;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner br;

    public Client() {

        try {
            socket = new Socket(ADDRESS, PORT);

            br = new Scanner(new InputStreamReader(System.in));
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("Вы подключились!!");

            Thread thread1 = new Thread(() -> {
                try {
                    while (true) {
                        String str1 = br.nextLine();
                        out.writeUTF(str1);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        System.out.println("Чат закрыт");
                        in.close();
                        out.close();
                        socket.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();

                        if(str.equals("/end")){
                            break;
                        }

                        System.out.println("От сервера пришло сообщение: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        System.out.println("Чат закрыт");
                        in.close();
                        out.close();
                        socket.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread1.setDaemon(true);
            thread2.setDaemon(true);

            thread1.start();
            thread2.start();

            while (true) {
                if (socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
