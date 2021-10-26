package commandsLib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Named socket used to write and read commands.
 * @author Luis Mariano Ramírez Segura
 */
public class CmdSocket {
    private Socket socket;  // connection socket
    private String name;  // socket name
    
    
    /**
     * Constructor that receives the socket data to create it
     * @param host Socket host
     * @param port Socket port
     * @throws IOException Error creating the socket
     */
    public CmdSocket(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.name = null;
    }
    
    
    /**
     * Constructor that receives the connection socket to set it directly.
     * @param socket Connection socket
     */
    public CmdSocket(Socket socket) {
        this.socket = socket;
        this.name = null;
    }   

    
    /**
     * Tries to read the next line from the socket as a command string.
     * @return Command string
     */
    public String readCommandString() {
        try {
            Scanner input = new Scanner(socket.getInputStream());
            if (input.hasNext()) {
                return input.nextLine();
            }
            return null;
            
        } catch (Exception e) {
            System.out.println(e);
            close();
            return null;
        }
    }
    
    
    /**
     * Sends a command through the socket.
     * @param command Command to send
     */
    public void send(Command command) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            output.println(command.toString());
            output.flush();
        } catch (IOException e) {
            System.out.println(e);
            close();
        }
    }
    
    
    /**
     * Indicates if the socket is has being disconnected from the server side.
     * @return If the socket is closed
     */
    public boolean isConnectedToServer() {
        return !socket.isClosed();
    }
    
    
    /**
     * Closes the socket if is not already closed.
     */
    public void close() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }
}
