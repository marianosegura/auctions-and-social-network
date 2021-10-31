package auctions.commands;

import auctions.ui.AuctionsServer;
import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class RequestUpdateBidderCmd extends Command {

    public RequestUpdateBidderCmd(CmdData data) {
        super(data);
    }
    
    public RequestUpdateBidderCmd(String name) {
        super();
        data.put("name", name);
    }

    @Override
    public void execute(Object context) {
        AuctionsServer server = (AuctionsServer) context;
        Command updateCmd = new UpdateBidderCmd(server.getAuctions());
        server.getCmdServer().send(updateCmd, data.get("name"));
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.REQUEST_UPDATE_BIDDER.toString();
    }
}
