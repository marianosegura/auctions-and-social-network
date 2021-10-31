/*
 */
package auctions.ui;

import auctions.commands.AuctionCmdsFactory;
import auctions.commands.ProposeBidCmd;
import auctions.commands.RequestUpdateBidderCmd;
import auctions.models.Auction;
import auctions.models.AuctionState;
import auctions.models.Bid;
import auctions.models.Product;
import commandsLib.CmdClient;
import commandsLib.Command;
import commandsLib.DisconnectClientCmd;
import commandsLib.NameSocketCmd;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class BidderClient extends javax.swing.JFrame {
    private CmdClient cmdClient;
    private ArrayList<Auction> auctions;
    private Auction selectedAuction;
    
    
    public BidderClient() {
        initComponents();
        this.auctions = new ArrayList<>();
        this.selectedAuction = null;
        displaySelectedAuction();
        
        String bidderName = JOptionPane.showInputDialog("Bidder name: ");
        
        if (bidderName == null) {
            System.out.println("No bidder name provided!");
            System.exit(1);
        }
        
        this.bidderNameLabel.setText(bidderName);
        
        try {
            this.cmdClient = new CmdClient("localhost", 8080);
            cmdClient.listen(new AuctionCmdsFactory(), this);
            cmdClient.setLogging(true);
            
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    cmdClient.send(new DisconnectClientCmd());  // call to disconnect socket from the server-side
                    System.exit(0);
                }
            });
                
            auctionsJList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String auctionName = auctionsJList.getSelectedValue();
                        selectedAuction = auctions.stream().filter(
                                auction -> auction.getName().equals(auctionName)
                            ).findFirst().orElse(null);
                        displaySelectedAuction();
                    }
                }
            });
            
            cmdClient.send(new NameSocketCmd(bidderName));  // name remote socket
            cmdClient.getCmdSocket().setName(bidderName);  // remote local socket
            try {
                Thread.sleep(500);  // sleep half second to ensure remote socket naming
            } catch (InterruptedException ex) {}
            cmdClient.send(new RequestUpdateBidderCmd(bidderName));  // request auctions
        
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

    }
    
    
    public void displayAuctions() {
        DefaultListModel<String> model = new DefaultListModel<>();
        auctions.forEach(auction -> model.addElement(auction.getName()));
        auctionsJList.setModel(model);
        selectedAuction = null;
        displaySelectedAuction();
    }
    
    
    private void displaySelectedAuction() {
        if (selectedAuction == null) {
            selectedAuctionNameLabel.setText("");
            selectedProductNameLabel.setText("");
            selectedPriceLabel.setText("");
            auctionNameLabel.setText("");
            stateLabel.setText("");
            
            productNameLabel.setText("");
            productBasePriceLabel.setText("");
            auctionNameLabel.setText("");
            productDescriptionText.setText("");
            auctionDescriptionText.setText("");
            auctioneerNameLabel.setText("");
            
            selectedDateLabel.setText("");
            startDateLabel.setText("");
            endDateLabel.setText("");
            
            auctionIconLabel.setVisible(false);
            productIconLabel.setVisible(false);
            
            bidAmountSpinner.setVisible(false);
            bidButton.setVisible(false);
            bidLabel.setText("");
            
        } else {
            Product product = selectedAuction.getProduct();
            selectedAuctionNameLabel.setText(selectedAuction.getName());
            selectedProductNameLabel.setText(product.getName());
            selectedPriceLabel.setText(String.valueOf(product.getBasePrice()));
            auctioneerNameLabel.setText(selectedAuction.getAuctioneerName());
            stateLabel.setText(selectedAuction.getState().toString());
            
            productNameLabel.setText(product.getName());
            productBasePriceLabel.setText(String.valueOf(product.getBasePrice()));
            auctionNameLabel.setText(selectedAuction.getName());
            productDescriptionText.setText(product.getDescription());
            auctionDescriptionText.setText(selectedAuction.getDescription());
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            selectedDateLabel.setText(dateFormat.format(selectedAuction.getEnd()));
            startDateLabel.setText(dateFormat.format(selectedAuction.getStart()));
            endDateLabel.setText(dateFormat.format(selectedAuction.getEnd()));
            
            auctionIconLabel.setVisible(true);
            productIconLabel.setVisible(true);
            try {
                BufferedImage auctionImage = ImageIO.read(new URL(selectedAuction.getImageUrl()));
                auctionIconLabel.setIcon(new ImageIcon(adjustImage(auctionImage)));
                BufferedImage productImage = ImageIO.read(new URL(product.getImageUrl()));
                productIconLabel.setIcon(new ImageIcon(adjustImage(productImage)));
            } catch (Exception ex) {}
            
            bidAmountSpinner.setVisible(false);
            bidButton.setVisible(false);
            if (selectedAuction.getState() == AuctionState.OPEN) {
                Bid bid = selectedAuction.getWinnerBid();
                SpinnerNumberModel spinnerModel = 
                        (bid == null) ? new SpinnerNumberModel(product.getBasePrice()+1, product.getBasePrice()+1, Integer.MAX_VALUE, 1)
                        : new SpinnerNumberModel(bid.getAmount()+1, bid.getAmount()+1, Integer.MAX_VALUE, 1);
                bidAmountSpinner.setModel(spinnerModel);
                bidAmountSpinner.setVisible(true);
                bidButton.setVisible(true);
            }
            
            if (selectedAuction.getWinnerBid() != null) {
                Bid bid = selectedAuction.getWinnerBid();
                bidLabel.setText(bid.getAmount() + " by " + bid.getBidderName());
            } else {
                bidLabel.setText("No bids made");
            }
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
    

    public ArrayList<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(ArrayList<Auction> auctions) {
        this.auctions = auctions;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        auctionsJList = new javax.swing.JList<>();
        titleLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        selectedProductNameLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        selectedPriceLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        selectedAuctionNameLabel = new javax.swing.JLabel();
        auctioneerNameLabel = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        bidLabel = new javax.swing.JLabel();
        bidButton = new javax.swing.JButton();
        bidAmountSpinner = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        selectedDateLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        bidderNameLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        productBasePriceLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        auctionNameLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        startDateLabel = new javax.swing.JLabel();
        End = new javax.swing.JLabel();
        endDateLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productDescriptionText = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        auctionDescriptionText = new javax.swing.JTextArea();
        productIconLabel = new javax.swing.JLabel();
        auctionIconLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        auctionsJList.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        auctionsJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(auctionsJList);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setText("Selected Auction");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Product:");

        selectedProductNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectedProductNameLabel.setText("productName");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Base Price:");

        selectedPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectedPriceLabel.setText("price");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Auction:");

        selectedAuctionNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectedAuctionNameLabel.setText("auctionName");
        selectedAuctionNameLabel.setToolTipText("");

        auctioneerNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        auctioneerNameLabel.setText("auctioneerName");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Best bid:");

        stateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stateLabel.setText("state");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("State:");

        bidLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bidLabel.setText("bid ");

        bidButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bidButton.setText("Bid");
        bidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bidButtonActionPerformed(evt);
            }
        });

        bidAmountSpinner.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Ends:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Auctioneer:");

        selectedDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        selectedDateLabel.setText("date");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Bidder:");

        bidderNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bidderNameLabel.setText("bidderName");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(selectedProductNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(selectedAuctionNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectedPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selectedDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(auctioneerNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bidLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bidderNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(bidAmountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(selectedAuctionNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(selectedProductNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(selectedPriceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(selectedDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(auctioneerNameLabel)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(stateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(bidLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(bidderNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bidAmountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Auction List", jPanel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setText("Product");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Name:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Base Price");

        productNameLabel.setText("name");

        productBasePriceLabel.setText("price");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel7.setText("Auction");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Name");

        auctionNameLabel.setText("name");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Start:");

        startDateLabel.setText("date");

        End.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        End.setText("End:");

        endDateLabel.setText("date");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Description:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Description");

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        productDescriptionText.setEditable(false);
        productDescriptionText.setColumns(20);
        productDescriptionText.setRows(5);
        jScrollPane1.setViewportView(productDescriptionText);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        auctionDescriptionText.setEditable(false);
        auctionDescriptionText.setColumns(20);
        auctionDescriptionText.setRows(5);
        jScrollPane2.setViewportView(auctionDescriptionText);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(productBasePriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(productIconLabel))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(75, 75, 75)
                        .addComponent(auctionIconLabel))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(auctionNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(startDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(End)
                        .addGap(18, 18, 18)
                        .addComponent(endDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(productIconLabel)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(productNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(productBasePriceLabel))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(auctionIconLabel)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(auctionNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(startDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(End)
                            .addComponent(endDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(209, 209, 209))
        );

        jTabbedPane2.addTab("Auction Info", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bidButtonActionPerformed
        Bid bid = new Bid(cmdClient.getCmdSocket().getName(), (int) bidAmountSpinner.getValue());
        Command bidCmd = new ProposeBidCmd(selectedAuction.getAuctioneerName(), bid);
        cmdClient.send(bidCmd);
    }//GEN-LAST:event_bidButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BidderClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel End;
    private javax.swing.JTextArea auctionDescriptionText;
    private javax.swing.JLabel auctionIconLabel;
    private javax.swing.JLabel auctionNameLabel;
    private javax.swing.JLabel auctioneerNameLabel;
    private javax.swing.JList<String> auctionsJList;
    private javax.swing.JSpinner bidAmountSpinner;
    private javax.swing.JButton bidButton;
    private javax.swing.JLabel bidLabel;
    private javax.swing.JLabel bidderNameLabel;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel productBasePriceLabel;
    private javax.swing.JTextArea productDescriptionText;
    private javax.swing.JLabel productIconLabel;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JLabel selectedAuctionNameLabel;
    private javax.swing.JLabel selectedDateLabel;
    private javax.swing.JLabel selectedPriceLabel;
    private javax.swing.JLabel selectedProductNameLabel;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
