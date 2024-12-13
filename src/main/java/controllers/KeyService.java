package controllers;

import dao.JDBCUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.sql.Connection;
import java.sql.PreparedStatement;


// Tạo key
public class KeyService {
    static JDBCUtil jdbcUtil = new JDBCUtil();
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }



    public static void savePublicKeyToDatabase(int userId, String publicKey) throws Exception {
        String query = "INSERT INTO user_keys (user_id, public_key) VALUES (?, ?)";
        try (Connection conn = jdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, publicKey);
            stmt.executeUpdate();
        }
    }
    public static void updateEndTimeForOldKeys(int userId) throws Exception {
        String query = "UPDATE user_keys SET end_time = NOW() WHERE user_id = ? AND end_time IS NULL";
        try (Connection conn = jdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    // Lưu private key vào một file trên hệ thống
    public static void savePrivateKeyToFile(PrivateKey privateKey, String filePath) throws IOException {
        // Lưu private key vào file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(privateKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
