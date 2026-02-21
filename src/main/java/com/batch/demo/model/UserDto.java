package com.batch.demo.model;

public class UserDto {
    private Long userId;
    private String userName;
    private String userEmail;

    public UserDto() {
    }

    public UserDto(Long userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "UserDto{" +
               "userId=" + userId +
               ", userName='" + userName + '\'' +
               ", userEmail='" + userEmail + '\'' +
               '}';
    }
}