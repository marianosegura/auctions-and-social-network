package auctions.commands;

import auctions.models.Auction;
import auctions.ui.BidderClient;
import commandsLib.CmdData;
import commandsLib.Command;
import java.util.ArrayList;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class UpdateBidderCmd extends Command {

    public UpdateBidderCmd(CmdData data) {
        super(data);
    }
    
    public UpdateBidderCmd(ArrayList<Auction> auctions) {
        super();
        if (!auctions.isEmpty()) {
            ArrayList<String> auctionsStrings = new ArrayList<>();
            for (Auction auction : auctions) {
                auctionsStrings.add(auction.toString());
            }
            data.put("auctions", String.join("!!", auctionsStrings)); 
        }
    }
    
    @Override
    public void execute(Object context) {
        BidderClient bidder = (BidderClient) context;
        
        ArrayList<Auction> auctions = new ArrayList<>();
        String auctionsData = data.get("auctions");
        if (auctionsData != null) {
            String[] auctionsStrings = auctionsData.split("!!");
            for (String auctionString : auctionsStrings) {
                auctions.add(new Auction(auctionString));
            }
        }
        bidder.setAuctions(auctions);
        bidder.displayAuctions(); 
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.UPDATE_BIDDER.toString();
    }
}
