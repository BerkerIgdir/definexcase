package com.example.demo.business.enums;

public enum CategoryLevel {
    ZERO (0),
    ONE (1),
    TWO (2),
    THREE (3),
    FOUR (4),
    FIVE (5);

    private static final double PRIORITY_CONSTANT = 0.08;
    private final int level;

    CategoryLevel (int level) {
        this.level = level;
    }

    public static double getPriority(CategoryLevel categoryLevel){
        return 1.0 + categoryLevel.level * PRIORITY_CONSTANT;
    }

    @Override
    public String toString(){
        return this.name ();
    }
}
