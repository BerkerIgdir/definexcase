package com.example.demo.business.sorter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.business.Product;
import com.example.demo.business.ProductToIndex;

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
                .peek (score -> System.out.println ("score of product: " + product.getId () + " is: " + score))
                .sum () * product.getCategory ().getPriority();
    }

    public List<SorterResult> sort(final List<Product> products){
        return products.stream ()
                .filter (product -> !product.getTitle ().isEmpty ())
                .map (product -> new SorterResult (product,calculate (product)))
                .peek (score -> System.out.println ("total score of product: " + score.getProduct ().getId () + " is: " + score.getScore ()))
                .sorted (comparingDouble (SorterResult::getScore).reversed ())
                .collect(Collectors.toList());
    }

}
