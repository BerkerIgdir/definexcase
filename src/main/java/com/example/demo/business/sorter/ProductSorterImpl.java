package com.example.demo.business.sorter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.business.product.Product;

import static java.util.Comparator.comparingDouble;

public class ProductSorterImpl implements ProductSorter<Product> {
    private final String wordToSearch;
    private final List<ProductToIndex <Double>> pointTransformers;

    public ProductSorterImpl (String wordToSearch ,
                              List <ProductToIndex <Double>> pointTransformers) {
        this.wordToSearch = wordToSearch;
        this.pointTransformers = Collections.unmodifiableList (pointTransformers);
    }

    private double calculate(final Product product){
        return pointTransformers.stream ()
                .map (fun -> fun.derivePoint (product.getTitle (),wordToSearch))
                .mapToDouble (Number::doubleValue)
                .sum () * product.getCategory ().getPriority();
    }

    public List<SorterResult> sort(final List<Product> products){
        return products.parallelStream ()
                .filter (product -> !product.getTitle ().isEmpty ())
                .map (product -> new SorterResult (product,calculate (product)))
                .sorted (comparingDouble (SorterResult::getScore).reversed ())
                .filter (sorterResult -> sorterResult.getScore () != 0)
                .collect(Collectors.toList());
    }

}
