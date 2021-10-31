package auctions.commands;

import auctions.models.Bid;
import auctions.ui.AuctionsServer;
import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class ProposeBidCmd extends Command {

    public ProposeBidCmd(CmdData data) {
        super(data);
    }
    
    public ProposeBidCmd(String auctioneerName, Bid bid) {
        super();
        data.put("auctioneerName", auctioneerName);
        data.put("bid", bid.toString());
    }

    @Override
    public void execute(Object context) {
        AuctionsServer server = (AuctionsServer) context;
        Bid bid = new Bid(data.get("bid"));
        String auctioneerName = data.get("auctioneerName");
        
        Command evaluateCmd = new EvaluateBidCmd(bid);
        server.getCmdServer().send(evaluateCmd, auctioneerName);
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.PROPOSE_BID.toString();
    }
}