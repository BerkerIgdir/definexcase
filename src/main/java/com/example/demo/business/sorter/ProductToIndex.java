package com.example.demo.business.sorter;

@FunctionalInterface
public interface ProductToIndex<T extends Number> {
    T derivePoint(String title,String wordToSearch);
}
