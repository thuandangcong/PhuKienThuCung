package models;

import controllers.HashService;
import java.util.List;

public class Order {
    private int id;  // ID của đơn hàng
    private int userId;  // ID người dùng
    private String orderHash;  // Hash của đơn hàng
    private String signature;  // Chữ ký của người dùng
    private List<OrderItem> orderItems;  // Danh sách các sản phẩm trong đơn hàng

    // Constructor để tạo một đơn hàng mới
    public Order(int userId, List<OrderItem> orderItems) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.orderHash = generateOrderHash();  // Tạo hash cho đơn hàng
    }

    // Phương thức tạo hash cho đơn hàng
    private String generateOrderHash() {
        try {
            // Tạo hash từ thông tin đơn hàng (ví dụ: ID người dùng, các sản phẩm và số lượng)
            String orderData = userId + orderItems.toString();
            return HashService.generateHash(orderData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getter và setter cho các trường
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getOrderHash() {
        return orderHash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
