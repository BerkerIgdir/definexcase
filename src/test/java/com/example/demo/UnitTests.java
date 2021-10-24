package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.business.Category;
import com.example.demo.business.Product;
import com.example.demo.business.sorter.ProductSorterImpl;
import com.example.demo.business.ProductToIndex;
import com.example.demo.business.enums.CategoryLevel;
import com.example.demo.business.sorter.SorterResult;

@ExtendWith (SpringExtension.class)
class UnitTests {

    private final List <Product> productList = new ArrayList <> ();
    private final ProductToIndex<Double> indexer1 = (prod, wordToSearch) ->
            Optional.of (prod)
                .map (title -> {
                    var demo = title.split (" ");
                    var index = IntStream.range(0, demo.length)
                            .filter(ind-> wordToSearch.equalsIgnoreCase (demo[ind]))
                            .findFirst()
                            .orElseGet (()-> -1);

                    return index == -1 ? null : 100.0 /(index +1);
                }).orElseGet (() -> 0.0);

    private final ProductToIndex<Double> indexer2 = (prod,wordToSearch) ->
            Optional.ofNullable (prod)
                    .map (title -> title.split (" "))
                    .map (str -> 100 * 1.0/str.length)
                    .orElseGet (() -> ((double) 0));

    private final BiFunction <List<String>,String,Double> demoLambda = (list,title) ->
            list.stream ()
                    .mapToDouble (str -> helperMethod (title,str))
                    .sum ();

    private final ProductToIndex<Double> multiWordIndexer = (prodTitle,wordToSearch) ->
            Optional.of (prodTitle)
                    .filter (title -> title.contains (wordToSearch))
                    .map (title -> helperMethod (title, wordToSearch.split (" ")[0]))
                    .orElseGet (() -> demoLambda.apply (Arrays.asList (wordToSearch.split (" ")),prodTitle));



    private double helperMethod(String title, String wordToSearch){
        var demo = title.split (" ");
        var index = IntStream.range(0, demo.length)
                .filter(ind-> wordToSearch.equalsIgnoreCase (demo[ind]))
                .findFirst()
                .orElseGet (()-> -1);

        return index == -1 ? 0.0 : 100.0 /(index +1);
    }

    @BeforeEach
    void init(){
        var product1 = new Product ();
        var product2 = new Product ();
        var product3 = new Product ();

        var category1 = new Category (CategoryLevel.FOUR,"accessory");
        var category2 = new Category (CategoryLevel.ONE,"cellphone");


        product1.setCategory (category1);
        product1.setTitle ("Iphone 11 blue labeled covered guaranteed");
        product1.setId (1L);

        product2.setCategory (category1);
        product2.setTitle ("Iphone 11 labeled covered guaranteed");
        product2.setId (2L);

        product3.setCategory (category2);
        product3.setTitle ("Blue BlackBerry 11 labeled covered guaranteed");
        product3.setId (3L);

        productList.add (product1);
        productList.add (product2);
        productList.add (product3);
    }

    @Test
    void sorterTest(){
        var wordToSearch = "11 Blackberry";
        var functionList = Arrays.asList (multiWordIndexer,indexer2);
        var sorter = new ProductSorterImpl (wordToSearch,functionList);
        var result = sorter.sort (productList)
                .stream ()
                .map (SorterResult::getProduct)
                .collect(Collectors.toList());
        result.forEach (product -> System.out.println (product.getTitle ()));
        Assertions.assertNotNull (result);
    }

}
