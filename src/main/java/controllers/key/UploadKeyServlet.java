package controllers.key;

import controllers.KeyService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet(name = "upload-key", value = "/upload-key")
public class UploadKeyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lưu public key vào CSDL
            int userId = Integer.parseInt(request.getParameter("user_id"));
            String publicKey = request.getParameter("public_key");
            KeyService.updateEndTimeForOldKeys(userId);
            KeyService.savePublicKeyToDatabase(userId, publicKey);

            // Đặt thông tin vào request để hiển thị thông báo và publicKey trên trang key.jsp
            request.setAttribute("publicKey", publicKey);
            request.setAttribute("message", "Public Key đã được tải lên và cập nhật thành công!");

            // Forward về key.jsp để hiển thị kết quả
            request.getRequestDispatcher("/WEB-INF/key.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error uploading public key");
        }
    }
}
