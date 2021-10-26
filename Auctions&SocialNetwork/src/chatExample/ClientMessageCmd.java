package chatExample;

import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class ClientMessageCmd extends Command {    
    
    
    public ClientMessageCmd(CmdData data) {
        super(data);
    }
    
    
    public ClientMessageCmd(String message) {
        super();
        data.put("message", message);
    }
   
    
    @Override
    public void execute(Object context) {
        ChatServer server = (ChatServer) context;
        String message = data.get("message");
        server.display(message);  // display in server
        
        Command serverMessageCmd = new ServerMessageCmd(message);
        server.getCmdServer().send(serverMessageCmd);  // send to other clients
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.CLIENT_MESSAGE.toString();
    }

}
