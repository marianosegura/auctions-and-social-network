package auctions.commands;

import auctions.models.Auction;
import auctions.ui.AuctioneerClient;
import commandsLib.CmdData;
import commandsLib.Command;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class UpdateAuctioneerCmd extends Command {

    public UpdateAuctioneerCmd(CmdData data) {
        super(data);
    }
    
    public UpdateAuctioneerCmd(Auction auction) {
        super();
        data.put("auction", auction.toString());
    }
    
    @Override
    public void execute(Object context) {
        AuctioneerClient auctioneer = (AuctioneerClient) context;
        Auction auction = new Auction(data.get("auction"));
        auctioneer.setAuction(auction);
        auctioneer.displayAuction();
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.UPDATE_AUCTIONEER.toString();
    }
}
