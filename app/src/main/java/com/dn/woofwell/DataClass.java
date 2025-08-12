package com.dn.woofwell;

public class DataClass {

    private String title;
    private String description;
    private String type;
    private String brand;
    private String price;

    private String youtube;
    private String image;
    private String age;
    private String key;
    private  String sellerName;
    private  String sellerId;
    private  String name;
    private  String review;


    public DataClass(String title, String description, String type, String brand, String price, String youtube, String image, String age ,String key,String sellerName, String sellerId) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.brand = brand;
        this.price = price;
        this.youtube = youtube;
        this.image = image;
        this.age = age;
        this.key = key;
        this.sellerName = sellerName;
        this.sellerId = sellerId;
    }

    public DataClass(){

    }


    public String getSellerName() {
        return sellerName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}