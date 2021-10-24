package com.example.demo.business;

import com.example.demo.business.enums.CategoryLevel;

public class Category {
    private final CategoryLevel categoryLevel;
    private String categoryName;

    public Category (CategoryLevel categoryLevel , String categoryName) {
        this.categoryLevel = categoryLevel;
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    public void setCategoryName(String name){
        this.categoryName = name;
    }

    public double getPriority () {
        return CategoryLevel.getPriority (categoryLevel);
    }
}
