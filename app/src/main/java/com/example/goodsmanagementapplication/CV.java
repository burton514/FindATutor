package com.example.goodsmanagementapplication;

import java.io.Serializable;
import java.util.List;

public class CV implements Serializable {
    private String imageUrl; // URL ảnh từ Firestore Storage
    private String fullName;
    private String email;
    private String dateOfBirth;
    private String homeTown;
    private String currentAddress;
    private String workExperience;
    private String achievements;
    private String selfIntroduction;
    private List<String> tags;
    private List<Friend> friends; // Đổi kiểu dữ liệu từ Map thành List

    // Constructor mặc định (cần thiết khi dùng Firestore)
    public CV() {
    }

    public CV(String fullName, String dateOfBirth) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public CV(String imageUrl, String fullName, String email, String dateOfBirth, String currentAddress, String homeTown, String workExperience, String achievements, String selfIntroduction, List<String> tags) {
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.currentAddress = currentAddress;
        this.homeTown = homeTown;
        this.workExperience = workExperience;
        this.achievements = achievements;
        this.selfIntroduction = selfIntroduction;
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
