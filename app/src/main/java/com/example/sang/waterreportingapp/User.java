package com.example.sang.waterreportingapp;

/**
 * Created by sang on 10/1/16.
 *
 * Represents a single user in the system
 *
 */
public class User {

    private String name = null;
    private String username = null;
    private String password = null;
    private String emailAddress = null;
    private String homeAddress = null;
    private String phoneNumber = null;
    private String userType = null;
    private String userTitle = null;


    public User(){

    }

    /**
     * Make a new user
     * @param username      the person's name
     * @param password     the person's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /* **********************
     * Getters and setters for properties
     */

    public String getName() {
        return name;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getEmailAddress() {
        return emailAddress;
    }


    public String getHomeAddress() {
        return homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setName(String name) {
        this.name=(name);
    }

    public void setUsername(String username) {
        this.username=(username);
    }

    public void setPassword(String password) {
        this.password=(password);
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress=(emailAddress);
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress=(homeAddress);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber=(phoneNumber);
    }

    public void setUserType(String userType) {

        this.userType = userType;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    /**
     * Check whether caller is the same user as other
     * @param o the user we are comparing to
     * @return whether the user is equal or not
     */
    @Override
    public boolean equals(Object o) {
        User other = (User) o;
        return other != null && (this.getName().equals(other.getName())) && (this.getUsername().equals(other.getUsername())) && (this.getPassword().equals(other.getPassword())) && (this.getEmailAddress().equals(other.getEmailAddress())) && (this.getHomeAddress().equals(other.getHomeAddress())) && (this.getUserTitle().equals(other.getUserTitle())) && (this.getUserType().equals(other.getUserType())) && (this.getPhoneNumber().equals(other.getPhoneNumber()));
    }
}
