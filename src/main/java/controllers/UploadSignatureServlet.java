package controllers;

import dao.JDBCUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/upload-signature")
public class UploadSignatureServlet extends HttpServlet {
    JDBCUtil jdbcUtil = new JDBCUtil();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String signature = request.getParameter("signature");

        String query = "UPDATE orders SET signature = ? WHERE id = ?";
        try (Connection conn = jdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, signature);
            stmt.setInt(2, orderId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                response.getWriter().println("Signature uploaded successfully!");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Order not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error uploading signature.");
        }
    }
}
