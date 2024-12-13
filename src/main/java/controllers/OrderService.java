package controllers;

import dao.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderService {
    static JDBCUtil jdbcUtil = new JDBCUtil();
    public static void saveOrderHash(int orderId, String orderHash) throws SQLException, ClassNotFoundException {
        String query = "UPDATE orders SET order_hash = ? WHERE id = ?";
        try (Connection conn = jdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, orderHash);
            stmt.setInt(2, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order hash saved successfully!");
            } else {
                System.out.println("Order not found.");
            }
        }
    }
}
