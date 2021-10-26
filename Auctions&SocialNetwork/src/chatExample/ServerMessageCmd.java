package chatExample;

import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class ServerMessageCmd extends Command {    
    
    
    public ServerMessageCmd(CmdData data) {
        super(data);
    }
    
    
    public ServerMessageCmd(String message) {
        super();
        data.put("message", message);
    }
   
    
    @Override
    public void execute(Object context) {
        ChatClient client = (ChatClient) context;
        client.display(data.get("message"));
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.SERVER_MESSAGE.toString();
    }

}
