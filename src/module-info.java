module joymarket {
    // Membutuhkan library JavaFX
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;

    // Membuka akses package agar bisa dijalankan oleh JavaFX
    exports main;
    exports view;
    exports controller;
    exports model;
    exports database;
}