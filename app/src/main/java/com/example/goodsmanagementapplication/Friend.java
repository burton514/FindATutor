package com.example.goodsmanagementapplication;

import java.io.Serializable;
import java.util.List;

public class Friend implements Serializable {
    private String name;
    private String email;
    private String lastMessage;
    private String timestamp;
    private String avatarUrl; // Thêm ảnh đại diện
    private List<ChatItem> chatlist;
    public Friend() {
    }

    public List<ChatItem> getChatlist() {
        return chatlist;
    }

    public void setChatlist(List<ChatItem> chatlist) {
        this.chatlist = chatlist;
    }

    public Friend(String name, String lastMessage, String timestamp, String avatarUrl) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.avatarUrl = avatarUrl;
    }

    public Friend(String name, String email, String lastMessage, String timestamp, String avatarUrl) {
        this.name = name;
        this.email = email;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

