package com.example.demo.business;


import java.util.Objects;

public class Product {
    private long id;
    private String title;
    private Category category;


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

}
