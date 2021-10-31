package auctions.ui;

import auctions.commands.AuctionCmdsFactory;
import auctions.commands.UpdateAuctioneerCmd;
import auctions.commands.UpdateBidderCmd;
import auctions.models.Auction;
import auctions.models.AuctionState;
import commandsLib.CmdServer;
import commandsLib.CmdSocket;
import commandsLib.Command;
import commandsLib.OnCmdSocketDisconnected;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class AuctionsServer implements OnCmdSocketDisconnected {
    private CmdServer cmdServer;
    private ArrayList<Auction> auctions;
    
    public AuctionsServer() {
        this.auctions = new ArrayList<>();
        try {
            cmdServer = new CmdServer(8080);
            cmdServer.setLogging(true);     
        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }
    
    public void start() {
        cmdServer.listen(new AuctionCmdsFactory(), this, this);  // passed as context and as hook onSocketClosed
            
        Thread closeAuctionsThread = new Thread() {
            public void run() {
                while (cmdServer.isRunning()) {
                    try {
                        Thread.sleep(2000);  // sleep 2s

                        for(Auction auction : auctions) {
                            boolean finishedAuction = auction.getState() == AuctionState.OPEN 
                                && new Date().after(auction.getEnd());

                            if (finishedAuction) {
                                auction.setState(AuctionState.CLOSED);  // update auction
                                System.out.println("Auction " + auction.getName() + " closed!");
                                cmdServer.send(new UpdateAuctioneerCmd(auction), auction.getAuctioneerName());  // update auctioneer
                                updateBiddersAuctions();  // update bidders
                            }
                        }
                    } catch (InterruptedException ex) { }
                }
            }
        };
        closeAuctionsThread.start();
            
        System.out.println("Input q anytime to stop the server.");  
        Scanner scanner = new Scanner(System.in);   
        while (cmdServer.isRunning()) {
            char readedChar = scanner.next().charAt(0);
            if (readedChar == 'q') {
                cmdServer.setRunning(false);  // stop sever
                System.exit(0);
            }
        }
    }

    @Override
    public void onCmdSocketDisconnected(String socketName) {
        auctions.removeIf(auction -> auction.getAuctioneerName().equals(socketName));  // remove auctioneer auctions
        updateBiddersAuctions();
    }
    
    
    public void updateBiddersAuctions() {
        Command updateCmd = new UpdateBidderCmd(auctions);
        for (CmdSocket client : cmdServer.getCmdSockets()) {
            if (client.getName() != null) {
                boolean isBidder = true;
                for (Auction auction : auctions) {
                    if (auction.getAuctioneerName().equals(client.getName())) {
                        isBidder = false;
                        break;
                    }
                }
                if (isBidder) {
                    cmdServer.send(updateCmd, client.getName());
                }
            }
        }
    }
    

    public CmdServer getCmdServer() {
        return cmdServer;
    }

    public ArrayList<Auction> getAuctions() {
        return auctions;
    }
    
    public static void main(String args[]) {
        System.out.println("-Auctions Server-");
        AuctionsServer server = new AuctionsServer();
        server.start();
    }
}
