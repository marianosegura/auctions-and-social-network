package chatExample;

import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class NamedClientMessageCmd extends Command {    
    
    
    public NamedClientMessageCmd(CmdData data) {
        super(data);
    }
    
    
    public NamedClientMessageCmd(String message, String name) {
        super();
        data.put("message", message);
        data.put("name", name);
    }
   
    
    @Override
    public void execute(Object context) {
        ChatServer server = (ChatServer) context;
        String message = data.get("message");
        String clientName = data.get("name");
        
        server.display(message);  // display in server
        
        Command serverMessageCmd = new ServerMessageCmd(message);
        server.getCmdServer().send(serverMessageCmd, clientName);  // send to named socket
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.NAMED_CLIENT_MESSAGE.toString();
    }

}
