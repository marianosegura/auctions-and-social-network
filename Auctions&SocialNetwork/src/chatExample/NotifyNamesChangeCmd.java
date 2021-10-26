package chatExample;

import commandsLib.CmdData;
import commandsLib.CmdSocket;
import commandsLib.Command;
import java.util.ArrayList;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class NotifyNamesChangeCmd extends Command {    
    @Override
    public void execute(Object context) {
        ChatServer server = (ChatServer) context;
        
        ArrayList<String> clientNames = new ArrayList<>();
        clientNames.add("All");
        for(CmdSocket socket: server.getCmdServer().getCmdSockets()) {
            String socketName = socket.getName();
            if (socketName != null) {
                clientNames.add(socketName);
            }
        }
        
        server.updateSendNameDropdown(clientNames);  // update server names
        
        Command updateClientNamesCmd = new UpdateClientNamesCmd(clientNames);
        server.getCmdServer().send(updateClientNamesCmd);  // send command to update all client's names dropdowns
        
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.NOTIFY_NAMES_CHANGE.toString();
    }

}
