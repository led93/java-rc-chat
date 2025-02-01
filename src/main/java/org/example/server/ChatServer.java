package org.example.server;

import org.example.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private final int port;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Message message, ClientHandler sender) {
       for (ClientHandler client : clients) {
           if (client != sender) {
               client.sendMessage(message);
           }
       }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        new ChatServer(5000).start();
    }
}
