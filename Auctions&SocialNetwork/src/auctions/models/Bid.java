package auctions.models;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class Bid {
    private String bidderName;
    private int amount;

    public Bid(String bidderName, int amount) {
        this.bidderName = bidderName;
        this.amount = amount;
    }
    
    public Bid(String bidString) {
        String[] values = bidString.split("__");
        this.bidderName = values[0];
        this.amount = Integer.valueOf(values[1]);
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return bidderName + "__" + amount;
    }
    
}
