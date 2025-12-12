package model;

public class Customer extends User {
	private double balance;

	public Customer(String idUser, String fullName, String email, String password, String gender, String phone,
			String address, String role, double balance) {
		super(idUser, fullName, email, password, gender, phone, address, role);
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

}