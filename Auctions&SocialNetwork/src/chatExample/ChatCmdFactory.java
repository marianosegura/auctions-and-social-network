package chatExample;

import commandsLib.CmdData;
import commandsLib.Command;
import commandsLib.ICmdFactory;
import commandsLib.NullCmd;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class ChatCmdFactory implements ICmdFactory {

    @Override
    public Command getCommand(String identifier, CmdData data) {
        ChatCmds cmdEnum = ChatCmds.valueOf(identifier);
        
        switch(cmdEnum) {
            case SERVER_MESSAGE:
                return new ServerMessageCmd(data);
            case CLIENT_MESSAGE:
                return new ClientMessageCmd(data);
            case NAMED_CLIENT_MESSAGE:
                return new NamedClientMessageCmd(data);
            case NOTIFY_NAMES_CHANGE:
                return new NotifyNamesChangeCmd();
            case UPDATE_CLIENT_NAMES:
                return new UpdateClientNamesCmd(data);
            default:
                return new NullCmd();
        }
    }

}
