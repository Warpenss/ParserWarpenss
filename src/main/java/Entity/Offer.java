package Entity;

public class Offer {
    String name;
    String brand;
    String color;
    String size;
    String price;
    String initialPrice;
    String description;
    String articleId;
    String shippingCosts;

    public Offer() {
    }

    public Offer(String name, String brand, String color, String size, String price, String initialPrice, String description, String articleId, String shippingCosts) {
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.price = price;
        this.initialPrice = initialPrice;
        this.description = description;
        this.articleId = articleId;
        this.shippingCosts = shippingCosts;
    }

    @Override
    public String toString() {
        return "Offer" +
                " \n name=" + name +
                " \n brand=" + brand +
                " \n color=" + color +
                " \n size=" + size +
                " \n price=" + price +
                " \n initialPrice=" + initialPrice +
                " \n description=" + description +
                " \n articleId=" + articleId +
                " \n shippingCosts=" + shippingCosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(String initialPrice) {
        this.initialPrice = initialPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(String shippingCosts) {
        this.shippingCosts = shippingCosts;
    }
}
