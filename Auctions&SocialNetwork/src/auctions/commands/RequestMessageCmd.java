package auctions.commands;

import auctions.ui.AuctionsServer;
import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class RequestMessageCmd extends Command {
    public RequestMessageCmd(CmdData data) {
        super(data);
    }
    
    public RequestMessageCmd(String message, String name) {
        super();
        data.put("message", message);
        data.put("name", name);
    }

    @Override
    public void execute(Object context) {
        AuctionsServer server = (AuctionsServer) context;
        Command messageCmd = new MessageCmd(data.get("message"));
        server.getCmdServer().send(messageCmd, data.get("name"));
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.REQUEST_MESSAGE.toString();
    }
}
