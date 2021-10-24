package com.example.demo.business.sorter;


import com.example.demo.business.Product;

public final class SorterResult {
    private final Product product;
    private final double score;

    public Product getProduct () {
        return product;
    }

    public double getScore () {
        return score;
    }

    public SorterResult (Product product , double score) {
        this.product = product;
        this.score = score;
    }
}
