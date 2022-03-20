package com.nimsoc.fire;

public class User {
    String userName;
    String emailAddress;
    String profileImage;

    public User() {
    }

    public User(String userName, String emailAddress, String profileImage) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
