package com.example.demo.pojo;


public final class ProductPojo {
    private final long Id;
    private final String name;
    private final CategoryPojo category;
    private final boolean isActive;

    public ProductPojo (long id , String name , CategoryPojo category , boolean isActive) {
        Id = id;
        this.name = name;
        this.category = category;
        this.isActive = isActive;
    }

}
