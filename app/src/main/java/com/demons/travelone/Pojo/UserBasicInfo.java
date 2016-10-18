package com.demons.travelone.Pojo;

/**
 * Created by kinjal on 30/8/16.
 */
public class UserBasicInfo {
    String gender;
    String contact;

    public UserBasicInfo(){}

    public UserBasicInfo(String gender, String contact)
    {
        this.gender=gender;
        this.contact=contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
