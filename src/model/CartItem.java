package model;

public class CartItem {
    // Field idCustomer jangan sampai hilang
    private String idCustomer;
    private String idProduct;
    private String productName;
    private double price;
    private int quantity;
    private double total;

    public CartItem(String idCustomer, String idProduct, String productName, double price, int quantity) {
        this.idCustomer = idCustomer; // Disimpan di sini
        this.idProduct = idProduct;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

    
    
    // Getters
//    public String getIdCustomer() { return idCustomer; }
//    public String getIdProduct() { return idProduct; }
//    public String getProductName() { return productName; }
//    public double getPrice() { return price; }
//    public int getQuantity() { return quantity; }
//    public double getTotal() { return total; }
}