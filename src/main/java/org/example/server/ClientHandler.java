package org.example.server;

import org.example.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private ObjectOutputStream out;
    private String clientName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {

            this.out = outputStream;
            this.clientName = (String) in.readObject(); //First message is the client's name
            System.out.println("Client " + this.clientName + " joined the chat!");

            Message message;
            while((message = (Message) in.readObject()) != null) {
                System.out.println("Received a message: " + message);
                server.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(clientName + " disconnected.");
        } finally {
            server.removeClient(this);
        }
    }

    public void sendMessage(Message message) {
       try {
           out.writeObject(message);
           out.flush();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
}
