/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;

/**
 *
 * @author erino
 *
 * It is used to get input from server simultaneously
 */
public class TaskReadThread implements Runnable {

    //private variables
    Socket socket;
    ChatController client;
    DataInputStream input;

    //constructor
    public TaskReadThread(Socket socket, ChatController client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        //continuously loop it
        while (true) {
            try {
                //Create data input stream
                input = new DataInputStream(socket.getInputStream());

                //get input from the client
                String message = input.readUTF();
                String username = input.readUTF();
                //append message of the Text Area of UI (GUI Thread)
                Platform.runLater(() -> {
                    //display the message in the textarea

                    client.viewMsg.appendText(message + "\n");
                    if (!client.onlineUsers.getItems().contains(username)) {
                        client.onlineUsers.getItems().add(username);
                    }

                });
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
