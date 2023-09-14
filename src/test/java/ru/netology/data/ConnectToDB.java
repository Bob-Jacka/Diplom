package ru.netology.data;

import lombok.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectToDB {
    private static final String dbUrl = System.getProperty("db.url");
    private static final String dbUser = System.getProperty("db.user");
    private static final String dbPass = System.getProperty("db.pass");

    public static TransactionData getLastPaymentData(String tableName) {
        String paymentSQL = "SELECT id, status FROM " + tableName + " ORDER BY created DESC LIMIT 1;";
        String id = null;
        String status = null;
        try (
                Connection conn = DriverManager.getConnection(
                        dbUrl, dbUser, dbPass
                );
                Statement paymentStmt = conn.createStatement()) {
            try (var req = paymentStmt.executeQuery(paymentSQL)) {
                if (req.next()) {
                    id = req.getString("id");
                    status = req.getString("status");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new TransactionData(id, status);
    }

    @Value
    public static class TransactionData {
        String id;
        String status;
    }
}
