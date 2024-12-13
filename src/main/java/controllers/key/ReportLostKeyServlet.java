package controllers.key;

import dao.JDBCUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "reportkey", value = "/reportkey")
public class ReportLostKeyServlet extends HttpServlet {
    JDBCUtil jdbcUtil = new JDBCUtil();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));

        try {
            String updateQuery = "UPDATE user_keys SET end_time = NOW() WHERE user_id = ? AND end_time IS NULL";
            try (Connection conn = jdbcUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Đặt thông báo và xóa public key khỏi request
            request.setAttribute("publicKey", null);
            request.setAttribute("message", "Key đã được báo mất và vô hiệu hóa thành công!");

            // Forward về key.jsp để hiển thị kết quả
            request.getRequestDispatcher("/WEB-INF/key.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
