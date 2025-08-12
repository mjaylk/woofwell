package com.dn.woofwell;

public class User {
    public String userId;
    public String name;
    public String phoneNumber;
    public String email;
    public String profileImageUrl;
    public String fullName;
    public String address;
    public String accountType;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public User( String name, String phoneNumber, String email, String userId, String accountType) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountType = accountType;
    }
}
