package auctions.ui;

import javax.swing.JOptionPane;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class ClientMain {
    public static void main(String args[]) {
        Object[] loginOptions = { "Auctioneer", "Bideer" };
        boolean loginAsAuctioneer = 0 == JOptionPane.showOptionDialog(null, "Login as:", "Login",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, loginOptions, loginOptions[0]);
       
        if (loginAsAuctioneer) {
            new AuctionForm().setVisible(true);
        } else {
            new BidderClient().setVisible(true);
        }
    }
}
