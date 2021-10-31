package auctions.commands;

import auctions.models.Bid;
import auctions.ui.AuctioneerClient;
import commandsLib.CmdData;
import commandsLib.Command;
import javax.swing.JOptionPane;


/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class EvaluateBidCmd extends Command {

    public EvaluateBidCmd(CmdData data) {
        super(data);
    }
    
    public EvaluateBidCmd(Bid bid) {
        super();
        data.put("bid", bid.toString());
    }
    @Override
    public void execute(Object context) {
        AuctioneerClient auctioneer = (AuctioneerClient) context;
        Bid bid = new Bid(data.get("bid"));
        
        String productName = auctioneer.getAuction().getProduct().getName();
        boolean bidAccepted = 0 == JOptionPane.showConfirmDialog(null, bid.getBidderName() + " is bidding " + bid.getAmount() + " for your " + productName + ". Do you accept?", "Bid", JOptionPane.YES_NO_OPTION);
        
        String bidderMessage = "Your bid of " + bid.getAmount() + " for " + productName + " was ";
        if (bidAccepted) {
            bidderMessage += "accepted!";
            auctioneer.getAuction().setWinnerBid(bid);
            auctioneer.displayAuction();
            auctioneer.getCmdClient().send(new UpsertAuctionCmd(auctioneer.getAuction()));
        } else {
            bidderMessage += "rejected.";
        }
        auctioneer.getCmdClient().send(new RequestMessageCmd(bidderMessage, bid.getBidderName()));
    }

    @Override
    public String getIdentifier() {
        return AuctionCmds.EVALUATE_BID.toString();
    }
}
