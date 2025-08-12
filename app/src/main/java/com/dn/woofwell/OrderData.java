package com.dn.woofwell;

public class OrderData {
    private String Address;
    private String Email;
    private String ItemCount;
    private String ItemName;
    private String ItemPrice;
    private String Phone;
    private String Image;

    private String FullName;
    private String Key;

    private  String SellerId;


    public OrderData() {
        // Required default constructor for Firebase
    }



    public OrderData(String address, String email, String itemCount, String itemName, String itemPrice, String phone, String image, String fullname) {
        Address = address;
        Email = email;
        ItemCount = itemCount;
        ItemName = itemName;
        ItemPrice = itemPrice;
        Phone = phone;
        Image = image;
        FullName = fullname;

    }


    public String getSellerId() {
        return SellerId;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getFullName() {
        return FullName;
    }

    public String getImage() {
        return Image;
    }

    public String getAddress() {
        return Address;
    }

    public String getEmail() {
        return Email;
    }

    public String getItemCount() {
        return ItemCount;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public String getPhone() {
        return Phone;
    }
}
