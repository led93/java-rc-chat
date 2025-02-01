package org.example.client;

import org.example.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final String host;
    private final int port;
    private String username;
    private ObjectOutputStream out;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
        start();
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            this.out = outputStream;

            //Set username
            System.out.println("Enter your name: ");
            username = scanner.nextLine();
            out.writeObject(username);
            out.flush();

            //Start listening thread
            new Thread(() -> {
                try {
                    while (true) {
                        Message message = (Message) in.readObject();
                        System.out.println("\n" + message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            //Sending messages
            while (true) {
                String text = scanner.nextLine();
                if(text.equalsIgnoreCase("exit")) break;
                out.writeObject(new Message(username, text));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatClient("localhost", 5000);
    }
}
