package controllers.key;

import controllers.KeyService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@WebServlet(name = "genkey", value = "/genkey")
public class GenerateKeyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Tạo cặp key
            KeyPair keyPair = KeyService.generateKeyPair();
            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            // Lưu public key vào CSDL
            int userId = Integer.parseInt(request.getParameter("user_id"));
            KeyService.updateEndTimeForOldKeys(userId);
            KeyService.savePublicKeyToDatabase(userId, publicKey);

            // Lưu private key vào file .txt cho người dùng tải về
            String privateKeyFileName = "private_key_" + userId + ".txt";  // Tên file riêng biệt cho mỗi người dùng
            String privateKeyFilePath = getServletContext().getRealPath("/") + privateKeyFileName;  // Đường dẫn file trên server

            try (FileWriter fw = new FileWriter(privateKeyFilePath)) {
                fw.write(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));  // Lưu private key dạng Base64
            }


            // Đặt thông tin vào request để hiển thị thông báo và publicKey trên trang key.jsp
            request.setAttribute("publicKey", publicKey);
            request.setAttribute("message", "Cặp key đã được tạo thành công!");
            request.setAttribute("privateKeyFileName", privateKeyFileName);
            // Forward về key.jsp để hiển thị kết quả
            request.getRequestDispatcher("/WEB-INF/key.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating keys");
        }
    }
}
