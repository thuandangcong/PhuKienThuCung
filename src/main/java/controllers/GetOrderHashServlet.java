package controllers;

import dao.JDBCUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/get-order-hash")
public class GetOrderHashServlet extends HttpServlet {
    JDBCUtil jdbcUtil = new JDBCUtil();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String query = "SELECT order_hash FROM orders WHERE id = ?";
        try (Connection conn = jdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("order_hash");
                response.getWriter().println(hash);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Order not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error fetching order hash.");
        }
    }
}

