package controllers;

import dao.JDBCUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/report-key")
public class ReportKeyServlet extends HttpServlet {
    JDBCUtil jdbcUtil = new JDBCUtil();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            String updateQuery = "UPDATE user_keys SET end_time = NOW() WHERE user_id = ? AND end_time IS NULL";
            try (Connection conn = jdbcUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            response.getWriter().println("Key reported and deactivated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}

