package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.demo.business.product.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.business.product.Product;
import com.example.demo.business.sorter.ProductSorterImpl;
import com.example.demo.business.sorter.ProductToIndex;
import com.example.demo.business.enums.CategoryLevel;
import com.example.demo.business.sorter.SorterResult;

@ExtendWith (SpringExtension.class)
class UnitTests {

    private final List <Product> productList = new ArrayList <> ();
    private final ProductToIndex<Double> indexer1 = (prod, wordToSearch) ->
            Optional.of (prod)
                .map (title -> {
                    var tokens = title.split (" ");
                    var index = IntStream.range(0, tokens.length)
                            .filter(ind-> wordToSearch.equalsIgnoreCase (tokens[ind]))
                            .findFirst()
                            .orElse (-1);

                    return index == -1 ? null : 100.0 /(index +1);
                }).orElseGet (() -> (double) 0);

    private final ProductToIndex<Double> indexer2 = (prod,wordToSearch) ->
            Optional.ofNullable (prod)
//                    .map (String::toUpperCase)
//                    .filter (title -> title.contains (wordToSearch))
                    .map (title -> title.split (" "))
                    .map (str -> 100 * 1.0/str.length)
                    .orElseGet (() -> ((double) 0));

    private final BiFunction <List<String>,String,Double> demoLambda = (list,title) ->
            list.stream ()
                    .mapToDouble (str -> helperMethod (title,str))
                    .sum ();

    private final ProductToIndex<Double> multiWordIndexer = (prodTitle,wordToSearch) ->
            Optional.of (prodTitle)
//                    .map (String::toUpperCase)
//                    .filter (title -> title.contains (wordToSearch))
                    .map (title -> helperMethod (title, wordToSearch.split (" ")[0]))
                    .orElse (0.0);


    private double helperMethod(String title, String wordToSearch){
        var tokens = title.split (" ");
        var index = IntStream.range(0, tokens.length)
                .filter(ind-> wordToSearch.equalsIgnoreCase (tokens[ind]))
                .findFirst()
                .orElseGet (()-> -1);
        if(index == -1 && wordToSearch.split (" ").length > 1){
            return Arrays.stream (wordToSearch.split (" "))
                    .mapToDouble (tkn -> helperMethod (title,tkn))
                    .sum ();
        }
        return index == -1 ? 0.0 : 100.0 /(index +1);
    }
    private double wordWeightBusinessLogic(String title, String wordToSearch){
        if(!title.contains (wordToSearch)) {
            if (wordToSearch.split (" ").length > 1) {
                return Arrays.stream (wordToSearch.split (" "))
                        .mapToDouble (tkn -> wordWeightBusinessLogic (title , tkn))
                        .sum ();
            }
            return 0.0;
        }

        var tokens = title.split (" ");
        return  100 * (wordToSearch.split (" ").length / (double)tokens.length);
    }

    @BeforeEach
    void init(){
        var accessoryCategory = new Category( CategoryLevel.FOUR,"accessory");
        var cellPhoneCategory = new Category( CategoryLevel.TWO,"cellphone");
        var product1 = new Product(1L,"IPhone 11 Spingen Mavi Pro Max Kilif",accessoryCategory,true);
        var product2 = new Product(2L,"IPhone 11 128 GB Cep telefonu",cellPhoneCategory,true);
        var product3 = new Product(3L,"IPhone 11 Pro Max Silikon Siyah Kilif",accessoryCategory,true);
        var product4 = new Product(4L,"Blackberry 11 Spingen Mavi Pro Max Kilif",accessoryCategory,true);

        productList.add (product1);
        productList.add (product2);
        productList.add (product3);
        productList.add (product4);
    }

    @Test
    void sorterTest(){
        var wordToSearch = "bLackBerry 12";
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
