package com.example.demo.service;

import com.example.demo.business.product.Product;
import com.example.demo.business.sorter.ProductSorterImpl;
import com.example.demo.business.sorter.ProductToIndex;
import com.example.demo.business.sorter.SorterResult;
import com.example.demo.dao.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
public class IndexerService {

    private final ProductRepo productRepo;
    private final BiFunction<List<String>,String,Double> wordPlaceIndexerMultiApplier = (list, title) ->
            list.stream ()
                    .mapToDouble (str -> wordPlaceIndexerBusinessLogic(title,str))
                    .sum ();

    private final ProductToIndex<Double> wordWeightIndexer = (prodTitle, wordToSearch) ->
            Optional.of (prodTitle)
                    .map (title -> title.split (" "))
                    .map (str -> 100 * 1.0/str.length)
                    .orElseGet (() -> ((double) 0));

    private final ProductToIndex<Double> wordPlaceIndexer = (prodTitle,wordToSearch) ->
            Optional.of (prodTitle)
                    .filter (title -> title.contains (wordToSearch))
                    .map (title -> wordPlaceIndexerBusinessLogic(title, wordToSearch.split (" ")[0]))
                    .orElseGet (() -> wordPlaceIndexerMultiApplier.apply (Arrays.asList (wordToSearch.split (" ")),prodTitle));

    public IndexerService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }



    public List<Product> index(String wordToSearch){
        var functionList = Arrays.asList (wordPlaceIndexer,wordWeightIndexer);
        var sorter = new ProductSorterImpl (wordToSearch,functionList);
        var productList =productRepo.getAllProducts();
        return sorter.sort(productList)
                .parallelStream()
                .map(SorterResult::getProduct)
                .collect(toList());
    }

    private double wordPlaceIndexerBusinessLogic(String title, String wordToSearch){
        var tokens = title.split (" ");
        var index = IntStream.range(0, tokens.length)
                .filter(ind-> wordToSearch.equalsIgnoreCase (tokens[ind]))
                .findFirst()
                .orElseGet (()-> -1);

        return index == -1 ? 0.0 : 100.0 /(index +1);
    }



}
