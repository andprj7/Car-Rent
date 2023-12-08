package com.example.caronrent;

public class ReadWriteUserDetails {
    public String name, email,pass,mobile, city, dll, gender,imageURLUser;

//    Name, Email, Pass,Mobile,City, Dll,Gender,uri.toString()
//    public ReadWriteUserDetails(String txtName,String txtEmail,String txtPass, String txtMobile, String City,  String txtDll,String txtGender,String imageURLUser) {
//
//    }


    public ReadWriteUserDetails(String name, String email, String pass, String mobile, String city, String dll, String gender, String imageURLUser) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.mobile = mobile;
        this.city = city;
        this.dll = dll;
        this.gender = gender;
        this.imageURLUser = imageURLUser;
    }
}