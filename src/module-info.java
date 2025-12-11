module joymarket {
    // Membutuhkan library JavaFX
    requires javafx.controls;
    requires javafx.graphics;
    
    // Membutuhkan library SQL untuk database
    requires java.sql;

    // Membuka akses package agar bisa dijalankan oleh JavaFX
    exports main;
    exports view;
    
    // Jika nanti ada error reflection di bagian lain, Anda bisa export package lainnya juga
    exports controller;
    exports model;
    exports database;
}