package com.sweetbaby.familyapp.family;

/**
 * Created by xiLab on 3/2/2018.
 */

public class Family {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(String address) {
        this.myLocation = myLocation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }

    private String name;
    private String myLocation;
    private String image;
    private String phone;
    private String relationship;
    private String myKey;
    public Family(){

    }
    public Family(String name, String myLocation, String image, String phone, String relationship,String myKey) {
        this.name = name;
        this.myLocation = myLocation;
        this.image = image;
        this.phone= phone;
        this.relationship = relationship;
        this.myKey = myKey;
    }


}

