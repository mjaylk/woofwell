package com.dn.woofwell;

public class Cart {
    private String itemName;
    private String itemCount;
    private String itemTotal;
    private String itemDesc;
    private String itemImage;
    private String singlePrice;
    private String youtube;
    private String brand;
    private String type;
    private String age;
    private String sellerName;
    private String sellerId;
    private String key;

    public Cart(String itemName, String itemCount, String itemTotal, String itemDesc, String itemImage, String singlePrice, String youtube, String brand, String type, String age, String sellerName, String sellerId, String key) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.itemTotal = itemTotal;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.singlePrice = singlePrice;
        this.youtube = youtube;
        this.brand = brand;
        this.type = type;
        this.age = age;
        this.sellerName = sellerName;
        this.sellerId = sellerId;
        this.key = key;
    }

    public Cart() {
    }


    public String getYoutube() {
        return youtube;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getAge() {
        return age;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getKey() {
        return key;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public String getItemDesc() {
        return itemDesc;
    }
}