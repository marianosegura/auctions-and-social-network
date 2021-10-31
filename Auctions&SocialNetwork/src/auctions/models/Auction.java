package auctions.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class Auction {
    private String name;
    private String description;
    private String imageUrl;
    private String auctioneerName;
    private Product product;
    private AuctionState state;
    private Date start;
    private Date end;
    private Bid winnerBid;

    public Auction(String name, String description, String imageUrl, String auctioneerName, Product product, Date end) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.auctioneerName = auctioneerName;
        this.product = product;
        this.state = AuctionState.OPEN;
        this.start = new Date();
        this.end = end;
        this.winnerBid = null;
    }
    
    public Auction(String auctionString) {
        String[] values = auctionString.split("--");
        this.name = values[0];
        this.description = values[1];
        this.imageUrl = values[2];
        this.auctioneerName = values[3];
        this.product = new Product(values[4]);
        this.state = AuctionState.valueOf(values[5]);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            this.start = dateFormat.parse(values[6]);
            this.end = dateFormat.parse(values[7]);
        } catch (Exception e) {
            System.out.println("Error parsing the auction dates! (" + name + ")");
            this.start = new Date();
            this.end = new Date();
        }
        
        this.winnerBid = null;
        String winnerBidString = values[8];
        if (winnerBidString.equals("null") == false) {
            this.winnerBid = new Bid(winnerBidString);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuctioneerName() {
        return auctioneerName;
    }

    public void setAuctioneerName(String auctioneerName) {
        this.auctioneerName = auctioneerName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Bid getWinnerBid() {
        return winnerBid;
    }

    public void setWinnerBid(Bid winnerBid) {
        this.winnerBid = winnerBid;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String toString = String.join("--", name, description, imageUrl, auctioneerName, product.toString(), state.toString(), dateFormat.format(start), dateFormat.format(end));
        if (winnerBid == null) {
            toString += "--null";
        } else {
            toString += "--" + winnerBid.toString();
        }
        return toString;
    }
}
