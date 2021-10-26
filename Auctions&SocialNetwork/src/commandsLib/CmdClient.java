package commandsLib;

import java.io.IOException;

/**
 * Command client. Handles the command socket to send and execute commands for a 
 * given context.
 * @author Luis Mariano Ramírez Segura
 */
public class CmdClient {
    private CmdSocket cmdSocket;  // command socket
    private boolean logging;  // print log received and sent commands flag
    
    
    /**
     * Construct that receives the host and port to create the 
     * command socket.
     * @param host Socket host
     * @param port Socket port
     * @throws IOException Error creating the command socket
     */
    public CmdClient(String host, int port) throws IOException {
        this.cmdSocket = new CmdSocket(host, port);
        this.logging = false;
    }
    
    
    /**
     * Starts an anonymous thread in which it listens for new commands on the
     * command socket while is opened. Upon command received, the given cmdFactory
     * is used to create a concrete command and the given context object is 
     * passed to the command for execution.
     * @param cmdFactory Commands factory, returns a concrete command for execution
     * @param context Context object being passed to the commands
     */
    public void listen(ICmdFactory cmdFactory, Object context) {
        Thread listenThread = new Thread() {  // start anonymous thread to read commands
            public void run() {
                while(cmdSocket.isConnectedToServer()) {   // keep reading for commands until the socket is disconnected from the server
                    String cmdString = cmdSocket.readCommandString();

                    if (cmdString != null) {
                        if (logging) System.out.println("(CmdClient) In: " + cmdString);
                        String cmdIdentifier = Command.parseIdentifier(cmdString);  // parse identifier
                        CmdData cmdData = Command.parseData(cmdString);  // parse data
                        Command command = cmdFactory.getCommand(cmdIdentifier, cmdData);  // get command from the factory
                        command.execute(context);  // pass the context to execute command
                    }
                }
            }
        };
        listenThread.start();
    }
    
    
    /**
     * Sends a command through the command socket.
     * @param command Command to send
     */
    public void send(Command command) {
        cmdSocket.send(command);
        if (logging) System.out.println("(CmdClient) Out: " + command.toString());
    }
    

    public CmdSocket getCmdSocket() {
        return cmdSocket;
    }
    
    
    public void setLogging(boolean logging) {
        this.logging = logging;
    }
}
