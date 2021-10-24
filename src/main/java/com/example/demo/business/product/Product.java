package com.example.demo.business.product;


import java.util.Objects;

public class Product {
    private long id;
    private String title;
    private Category category;

    public Product(long id, String title, Category category, boolean isActive) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.isActive = isActive;
    }

    private boolean isActive;


    public long getId () {
        return id;
    }

    public void setId (long id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String name) {
        Objects.requireNonNull (name);
        this.title = name;
    }

    public Category getCategory () {
        return category;
    }

    public void setCategory (Category category) {
        this.category = category;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }
}
