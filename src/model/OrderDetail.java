package model;

public class OrderDetail {
	private String idOrder;
	private String idProduct;
	private int quantity;

	private String productName;
	private double productPrice;

	public OrderDetail() {
	}

	public OrderDetail(String idOrder, String idProduct, int quantity) {
		this.idOrder = idOrder;
		this.idProduct = idProduct;
		this.quantity = quantity;
	}
	
	public String getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(String idOrder) {
		this.idOrder = idOrder;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
}