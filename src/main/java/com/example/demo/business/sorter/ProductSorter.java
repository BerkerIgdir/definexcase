package com.example.demo.business.sorter;


import java.util.List;

import com.example.demo.business.Product;

@FunctionalInterface
public interface ProductSorter<T extends Product> {
    List <SorterResult> sort(List<T> listToSort);
}
