package controllers.key;

import dao.KeyDao;
import models.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "key-info", value = "/key-info")
public class KeyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // update public key trong UserSection
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.setAttribute("errorMessage", "Vui lòng đăng nhập.");
                request.getRequestDispatcher("/WEB-INF/sign-in.jsp").forward(request, response); // Hiển thị lỗi trên trang login.jsp
            }
            String publicKey = KeyDao.getPublicKeyByUserId(user.getId());
            user.setPublicKey(publicKey);
            session.setAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}