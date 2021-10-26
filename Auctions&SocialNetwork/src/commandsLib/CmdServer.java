package commandsLib;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Command server. Handles socket connections and commands execution for a 
 * given context.
 * @author Luis Mariano Ramírez Segura
 */
public class CmdServer {
    private ServerSocket serverSocket;  // server socket
    private ArrayList<CmdSocket> cmdSockets;  // connected command sockets
    private boolean running;  // indicates if the server is active, can be used to stop all server threads
    private boolean logging;  // print log received and sent commands flag
    
    
    /**
     * Constructor that receives the port used to listen for sockets.
     * @param port Server port
     * @throws IOException Error creating the server socket
     */
    public CmdServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.cmdSockets = new ArrayList<>();
        this.running = true;
        this.logging = false;
    }
    

    /**
     * Starts an anonymous thread in which new sockets are accepted. For
     * each new socket connected an anonymous thread started to listen for
     * commands. Upon command received, the given cmdFactory is used to create 
     * a concrete command and the given context object is passed to the command 
     * for execution. The only exceptions for the command factory are the NameSocketCmd  
     * (used to name a socket and target a specific socket when sending) and the DisconnectClinetCmd
     * (used to disconnect the socket from this server).
     * @param cmdFactory Commands factory, returns a concrete command for execution
     * @param context Context object being passed to the commands
     */
    public void listen(ICmdFactory cmdFactory, Object context) {
        Thread listenThread = new Thread() {  // start anonymous thread to accept connection sockets
            public void run() {
                while (running) {  // accept sockets while the server is running
                    try {
                        CmdSocket cmdSocket = new CmdSocket(serverSocket.accept());  // accept new socket
                        cmdSockets.add(cmdSocket);  // add new socket
                        
                        if (logging) System.out.println("(CmdServer) New socket connected from " + ((InetSocketAddress)cmdSocket.getSocket().getRemoteSocketAddress()).getAddress().toString());
                        
                        Thread socketThread = new Thread() {  // start anonymous thread to listen socket commands
                            public void run() {
                                while(running) {   // keep listening while server is running and socket is open
                                    String cmdString = cmdSocket.readCommandString();
                                    
                                    if (cmdString != null) {
                                        if (logging) System.out.println("(CmdServer) " + cmdSocket.getName() + " sent: " + cmdString);
                                        String cmdIdentifier = Command.parseIdentifier(cmdString);
                                        CmdData cmdData = Command.parseData(cmdString);
                                        
                                        if (cmdIdentifier.equals("DISCONNECT_CLIENT")) break;

                                        if (cmdIdentifier.equals("NAME_SOCKET")) {  // only command exception to factory is for socket naming
                                            new NameSocketCmd(cmdData).execute(cmdSocket);  // pass client socket to be named
                                        } else {
                                            Command command = cmdFactory.getCommand(cmdIdentifier, cmdData);
                                            command.execute(context);  // pass the server context to execute command
                                        }
        
                                    }
                                    
                                }
                                cmdSocket.close();
                                cmdSockets.remove(cmdSocket);  // remove socket when closed
                            }
                        };
                        socketThread.start();
                        
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                } 
            }
            
        };
        listenThread.start();
        
    }
    
    /**
     * Starts an anonymous thread in which new sockets are accepted. For
     * each new socket connected an anonymous thread started to listen for
     * commands. Upon command received, the given cmdFactory is used to create 
     * a concrete command and the given context object is passed to the command 
     * for execution. The only exceptions for the command factory are the NameSocketCmd  
     * (used to name a socket and target a specific socket when sending) and the DisconnectClinetCmd
     * (used to disconnect the socket from this server).
     * Receives a hook to call when a named socket is disconnected.
     * @param cmdFactory Commands factory, returns a concrete command for execution
     * @param context Context object being passed to the commands
     * @param socketDisconnectedHook Hook called when a CmdSocket is disconnected
     */
    public void listen(ICmdFactory cmdFactory, Object context, OnCmdSocketDisconnected socketDisconnectedHook) {
        Thread listenThread = new Thread() {  // start anonymous thread to accept connection sockets
            public void run() {
                while (running) {  // accept sockets while the server is running
                    try {
                        CmdSocket cmdSocket = new CmdSocket(serverSocket.accept());  // accept new socket
                        cmdSockets.add(cmdSocket);  // add new socket
                        
                        if (logging) System.out.println("(CmdServer) New socket connected from " + ((InetSocketAddress)cmdSocket.getSocket().getRemoteSocketAddress()).getAddress().toString());
                        
                        Thread socketThread = new Thread() {  // start anonymous thread to listen socket commands
                            public void run() {
                                while(running) {   // keep listening while server is running and socket is open
                                    String cmdString = cmdSocket.readCommandString();
                                    
                                    if (cmdString != null) {
                                        if (logging) System.out.println("(CmdServer) In (" + cmdSocket.getName() + "): " + cmdString);
                                        String cmdIdentifier = Command.parseIdentifier(cmdString);
                                        CmdData cmdData = Command.parseData(cmdString);
                                        
                                        if (cmdIdentifier.equals("DISCONNECT_CLIENT")) break;

                                        if (cmdIdentifier.equals("NAME_SOCKET")) {  // only command exception to factory is for socket naming
                                            new NameSocketCmd(cmdData).execute(cmdSocket);  // pass client socket to be named
                                        } else {
                                            Command command = cmdFactory.getCommand(cmdIdentifier, cmdData);
                                            command.execute(context);  // pass the server context to execute command
                                        }
        
                                    }
                                    
                                }
                                cmdSocket.close();
                                cmdSockets.remove(cmdSocket);  // remove socket when closed
                                if (socketDisconnectedHook != null && cmdSocket.getName() != null) {
                                    socketDisconnectedHook.onCmdSocketDisconnected(cmdSocket.getName());
                                }
                            }
                        };
                        socketThread.start();
                        
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                } 
            }
            
        };
        listenThread.start();
        
    }
    
    
    /**
     * Sends a command to all sockets.
     * @param command Command to send
     */
    public void send(Command command) {
        for (CmdSocket socket : cmdSockets) {
            if (logging) System.out.println("(CmdServer) Out (" + socket.getName() + "): " + command.toString());
            socket.send(command);
        }
    }
    
    
    /**
     * Sends a command to all sockets that match a given name.
     * @param command Command to send
     * @param socketName Socket name
     */
    public void send(Command command, String socketName) {
        for (CmdSocket socket : cmdSockets) {
            if (socketName.equals(socket.getName())) {
                if (logging) System.out.println("(CmdServer) Out (" + socket.getName() + "): " + command.toString());
                socket.send(command);
            }
        }
    }
    

    public ArrayList<CmdSocket> getCmdSockets() {
        return cmdSockets;
    }
    

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
    

    public void setRunning(boolean running) {
        this.running = running;
    }
}

