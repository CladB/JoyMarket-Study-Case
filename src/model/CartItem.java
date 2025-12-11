package model;

public class CartItem {
    private String idCustomer;
    private String idProduct;
    private String productName;
    private double price;
    private int quantity;
    private double total;

    public CartItem(String idCustomer, String idProduct, String productName, double price, int quantity) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    // Getters untuk PropertyValueFactory TableView
    public String getIdCustomer() { return idCustomer; }
    public String getIdProduct() { return idProduct; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
}