package auctions.commands;

import auctions.models.Auction;
import auctions.ui.AuctionsServer;
import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class UpsertAuctionCmd extends Command {

    public UpsertAuctionCmd(CmdData data) {
        super(data);
    }
    
    public UpsertAuctionCmd(Auction auction) {
        super();
        data.put("auction", auction.toString());
    }

    @Override
    public void execute(Object context) {
        AuctionsServer server = (AuctionsServer) context;
        Auction auction = new Auction(data.get("auction"));
        
        server.getAuctions().removeIf(auct -> auct.getName().equals(auction.getName()));  // remove if exists (to override)
        server.getAuctions().add(auction);  // add
        server.updateBiddersAuctions();
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.UPSERT_AUCTION.toString();
    }
}
