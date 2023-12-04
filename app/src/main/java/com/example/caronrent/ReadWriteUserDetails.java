package com.example.caronrent;

public class ReadWriteUserDetails {
    public String name, mobile, email, dll, pass,city,gender;

    public ReadWriteUserDetails(String txtName, String txtMobile, String txtEmail, String txtDll, String txtPass,String City,String txtGender
    ) {
        this.name = txtName;
        this.mobile = txtMobile;
        this.email = txtEmail;
        this.dll = txtDll;
        this.pass = txtPass;
        this.city=City;
        this.gender=txtGender;
    }
}
