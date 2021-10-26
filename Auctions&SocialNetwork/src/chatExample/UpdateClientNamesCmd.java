package chatExample;

import commandsLib.CmdData;
import commandsLib.Command;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class UpdateClientNamesCmd extends Command {    
    
    
    public UpdateClientNamesCmd(CmdData data) {
        super(data);
    }
    
    
    public UpdateClientNamesCmd(ArrayList<String> names) {
        super();
        data.put("names", String.join(",", names));
    }
   
    
    @Override
    public void execute(Object context) {
        ChatClient client = (ChatClient) context;
        
        String[] namesArray = data.get("names").split(",");
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(namesArray));  // to array list
        client.updateSendNameDropdown(names);  // update client
    }
    

    @Override
    public String getIdentifier() {
        return ChatCmds.UPDATE_CLIENT_NAMES.toString();
    }

}
