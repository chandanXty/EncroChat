package com.ctyx.encrochat;

public class users {

    public String name;
    public String image;
    public String  status;
    public String firebaseId;
    public users(){

    }

    public users(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }


}
