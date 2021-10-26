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
    
    
    public NamedClientMessageCmd(String message, String receiverName, String senderName) {
        super();
        data.put("message", message);
        data.put("receiver", receiverName);
        data.put("sender", senderName);
    }
   
    
    @Override
    public void execute(Object context) {
        ChatServer server = (ChatServer) context;
        String message = data.get("message");
        String receiverName = data.get("receiver");
        String senderName = data.get("sender");
        
        server.display(message);  // display in server
        
        Command serverMessageCmd = new ServerMessageCmd(message);
        server.getCmdServer().send(serverMessageCmd, receiverName);  // send to receiver
        
        if (!receiverName.equals(senderName)) {  // don't send message twice if the client sent the message to himself
            server.getCmdServer().send(serverMessageCmd, senderName);  // send message to sender too
        }
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.NAMED_CLIENT_MESSAGE.toString();
    }

}
