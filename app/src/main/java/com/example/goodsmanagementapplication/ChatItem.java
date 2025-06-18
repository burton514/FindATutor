package com.example.goodsmanagementapplication;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class ChatItem implements Serializable {
    private String avatarUrl; // Link ảnh đại diện
    private String email; // Email người gửi
    private String name; // Tên người gửi
    private String lastMessage; // Nội dung tin nhắn cuối
    private String timestamp; // Thời gian tin nhắn

    public ChatItem() {
    }

    // Constructor
    public ChatItem(String avatarUrl, String email, String name, String lastMessage, String timestamp) {
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    // Getters
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters (nếu cần thay đổi dữ liệu)
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
