package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8189;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner br;


    public Server(){
        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер подключен!");
            socket = server.accept();
            System.out.println("Клиент подключился!");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            br = new Scanner(new InputStreamReader(System.in));

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
                try{
                    while (true){
                        String str = in.readUTF();

                        if(str.equals("/end")){
                            break;
                        }
                        System.out.println("От клиента пришло сообщение: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Чат закрыт");
                    try {
                        in.close();
                        out.close();
                        socket.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        server.close();
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
                    try {
                        server.close();
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
