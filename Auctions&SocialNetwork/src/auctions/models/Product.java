package auctions.models;

/**
 *
 * @author Luis Mariano Ramírez Segura
 */
public class Product {
    private String name;
    private String description;
    private String imageUrl;
    private int basePrice;

    
    public Product(String name, String description, String imageUrl, int basePrice) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
    }
    
    
    public Product(String productString) {
        String[] values = productString.split("__");
        this.name = values[0];
        this.description = values[1];
        this.imageUrl = values[3];
        this.basePrice = Integer.valueOf(values[2]);
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

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return String.join("__", name, description, String.valueOf(basePrice), imageUrl);
    }
}
