/*
 */
package auctions.ui;

import auctions.commands.RequestMessageCmd;
import auctions.commands.AuctionCmdsFactory;
import auctions.commands.UpsertAuctionCmd;
import auctions.models.Auction;
import auctions.models.AuctionState;
import auctions.models.Bid;
import auctions.models.Product;
import commandsLib.CmdClient;
import commandsLib.DisconnectClientCmd;
import commandsLib.NameSocketCmd;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class AuctioneerClient extends javax.swing.JFrame {
    private CmdClient cmdClient;
    private Auction auction;

    
    public AuctioneerClient(Auction auction) {
        initComponents();
        this.auction = auction;
        
        try {
            this.cmdClient = new CmdClient("localhost", 8080);
            cmdClient.listen(new AuctionCmdsFactory(), this);
            cmdClient.setLogging(true);

            cmdClient.send(new NameSocketCmd(auction.getAuctioneerName()));  // name socket
            cmdClient.send(new UpsertAuctionCmd(auction));  // create auction

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    cmdClient.send(new DisconnectClientCmd());  // call to disconnect socket from the server-side
                    System.exit(0);
                }
            });

            displayAuction();
            
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    
    public void displayAuction() {
        titleLabel.setText(auction.getAuctioneerName() + "'s Auction");
        
        auctionNameLabel.setText(auction.getName());
        auctionDescriptionText.setText(auction.getDescription());
        auctionStateLabel.setText(auction.getState().toString());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        startDateLabel.setText(dateFormat.format(auction.getStart()));
        endDateLabel.setText(dateFormat.format(auction.getEnd()));
        
        
        Product product = auction.getProduct();
        productNameLabel.setText(product.getName());
        productBasePriceLabel.setText(String.valueOf(product.getBasePrice()));
        productDescriptionText.setText(product.getDescription());
        
        try {
            BufferedImage auctionImage = ImageIO.read(new URL(auction.getImageUrl()));
            auctionIconLabel.setIcon(new ImageIcon(adjustImage(auctionImage)));
            BufferedImage productImage = ImageIO.read(new URL(product.getImageUrl()));
            productIconLabel.setIcon(new ImageIcon(adjustImage(productImage)));
        } catch (Exception ex) {}
        
        
        Bid bid = auction.getWinnerBid();
        if (bid == null) {
            bidLabel.setText("No bids made");
        } else {
            bidLabel.setText(bid.getAmount() + " by " + bid.getBidderName());
        }
        
        
        messageInput.setVisible(false);
        messageWinnerButton.setVisible(false);
        cancelButton.setEnabled(false);
        closeButton.setEnabled(false);
        if (auction.getState() == AuctionState.OPEN) {
            cancelButton.setEnabled(true);
            closeButton.setEnabled(true);
        } 
        
        if (auction.getState() == AuctionState.CLOSED && auction.getWinnerBid() != null) {
            messageInput.setVisible(true);
            messageWinnerButton.setVisible(true);
        } 
    }
    
    
    private BufferedImage adjustImage(BufferedImage img) {
        int height = 50; 
        int width = 50; 
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    public CmdClient getCmdClient() {
        return cmdClient;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        End = new javax.swing.JLabel();
        startDateLabel = new javax.swing.JLabel();
        endDateLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productDescriptionText = new javax.swing.JTextArea();
        auctionNameLabel = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        productBasePriceLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        auctionDescriptionText = new javax.swing.JTextArea();
        productIconLabel = new javax.swing.JLabel();
        auctionIconLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        auctionStateLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        bidLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        messageInput = new javax.swing.JTextField();
        messageWinnerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titleLabel.setBackground(new java.awt.Color(102, 255, 255));
        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Auction");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setText("Product");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Name:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Description:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Base Price");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setText("Auction");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Name");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Description");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Start:");

        End.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        End.setText("End:");

        startDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        startDateLabel.setText("date");

        endDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        endDateLabel.setText("date");

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        productDescriptionText.setEditable(false);
        productDescriptionText.setColumns(20);
        productDescriptionText.setRows(5);
        jScrollPane1.setViewportView(productDescriptionText);

        auctionNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        auctionNameLabel.setText("name");

        productNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        productNameLabel.setText("name");

        productBasePriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        productBasePriceLabel.setText("price");

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        auctionDescriptionText.setEditable(false);
        auctionDescriptionText.setColumns(20);
        auctionDescriptionText.setRows(5);
        jScrollPane2.setViewportView(auctionDescriptionText);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Auction State:");

        auctionStateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        auctionStateLabel.setText("state");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Best bid:");

        bidLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bidLabel.setText("bid");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        messageWinnerButton.setText("Message Winner");
        messageWinnerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageWinnerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(productIconLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(productNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(productBasePriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(55, 55, 55)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel5)))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageInput, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageWinnerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(auctionStateLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(bidLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cancelButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(closeButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(32, 32, 32)
                                .addComponent(auctionIconLabel))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(auctionNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(startDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(End)
                                .addGap(18, 18, 18)
                                .addComponent(endDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(auctionIconLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(auctionNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(startDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(End)
                            .addComponent(endDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(productIconLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(productNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(productBasePriceLabel))
                        .addGap(28, 28, 28)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(auctionStateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bidLabel)
                    .addComponent(jLabel8))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(closeButton)
                    .addComponent(messageInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messageWinnerButton))
                .addGap(47, 47, 47))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        auction.setState(AuctionState.CANCELLED);
        displayAuction();
        cmdClient.send(new UpsertAuctionCmd(auction));
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        auction.setState(AuctionState.CLOSED);
        displayAuction();
        cmdClient.send(new UpsertAuctionCmd(auction));
    }//GEN-LAST:event_closeButtonActionPerformed

    private void messageWinnerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageWinnerButtonActionPerformed
        String message = messageInput.getText();
        cmdClient.send(new RequestMessageCmd(message, auction.getWinnerBid().getBidderName()));  // send message to winner
    }//GEN-LAST:event_messageWinnerButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel End;
    private javax.swing.JTextArea auctionDescriptionText;
    private javax.swing.JLabel auctionIconLabel;
    private javax.swing.JLabel auctionNameLabel;
    private javax.swing.JLabel auctionStateLabel;
    private javax.swing.JLabel bidLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField messageInput;
    private javax.swing.JButton messageWinnerButton;
    private javax.swing.JLabel productBasePriceLabel;
    private javax.swing.JTextArea productDescriptionText;
    private javax.swing.JLabel productIconLabel;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
