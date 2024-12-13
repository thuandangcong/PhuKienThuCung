package controllers.key;

import dao.KeyDao;
import models.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

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
            String publicKey = request.getParameter("public_key").trim();
            KeyDao.updateEndTimeForOldKeys(userId);
            KeyDao.savePublicKeyToDatabase(userId, publicKey);

            // Đặt thông tin vào request để hiển thị thông báo và publicKey trên trang key.jsp
//            request.setAttribute("publicKey", publicKey);
            HttpSession session = request.getSession();
            // update public key trong UserSection
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.setAttribute("errorMessage", "Vui lòng đăng nhập.");
                request.getRequestDispatcher("/WEB-INF/sign-in.jsp").forward(request, response); // Hiển thị lỗi trên trang login.jsp
            }
            user.setPublicKey(publicKey);
            session.setAttribute("user", user);
            request.setAttribute("message", "Public Key đã được tải lên và cập nhật thành công!");

            // Forward về key.jsp để hiển thị kết quả
            request.getRequestDispatcher("/WEB-INF/key.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error uploading public key");
        }
    }
}
