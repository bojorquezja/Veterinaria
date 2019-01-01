package main.java.com.wonen.veterinaria.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDeDatos {
    public static Connection getConnection() throws SQLException {
        String DB_URL = "jdbc:mysql://localhost:3306/EMP?useSSL=false";
        String USUAR = "usuario";
        String CONTR = "contrasena";
        return DriverManager.getConnection(DB_URL,USUAR,CONTR);
    }
}
