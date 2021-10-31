package auctions.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class MessageCmd extends Command {

    public MessageCmd(CmdData data) {
        super(data);
    }
    
    public MessageCmd(String message) {
        super();
        data.put("message", message);
    }
    
    @Override
    public void execute(Object context) {
        JOptionPane.showMessageDialog(null, data.get("message"), "Server Mesage", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.MESSAGE.toString();
    }
}
