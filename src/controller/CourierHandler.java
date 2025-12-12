package controller;

import java.util.List;
import database.CourierDA;
import model.Courier;

public class CourierHandler {
    
    private CourierDA courierDA = new CourierDA();

    // === Method untuk mengambil semua data kurir ===
    public List<Courier> getAllCouriers() {
        return courierDA.read();
    }
}