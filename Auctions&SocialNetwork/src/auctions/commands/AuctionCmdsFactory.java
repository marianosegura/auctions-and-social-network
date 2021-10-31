package auctions.commands;

import commandsLib.CmdData;
import commandsLib.Command;
import commandsLib.ICmdFactory;
import commandsLib.NullCmd;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class AuctionCmdsFactory implements ICmdFactory {

    @Override
    public Command getCommand(String identifier, CmdData data) {
        AuctionCmds cmdEnum = AuctionCmds.valueOf(identifier);
        switch(cmdEnum) {
            case UPSERT_AUCTION:
                return new UpsertAuctionCmd(data);
            case REQUEST_MESSAGE:
                return new RequestMessageCmd(data);
            case MESSAGE:
                return new MessageCmd(data);
            case PROPOSE_BID:
                return new ProposeBidCmd(data);
            case EVALUATE_BID:
                return new EvaluateBidCmd(data);
            case UPDATE_AUCTIONEER:
                return new UpdateAuctioneerCmd(data);
            case UPDATE_BIDDER:
                return new UpdateBidderCmd(data);
            case REQUEST_UPDATE_BIDDER:
                return new RequestUpdateBidderCmd(data);
            default:
                return new NullCmd();
        }
    }

}
