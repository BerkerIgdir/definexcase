package com.example.demo.service;

import com.example.demo.business.product.Product;
import com.example.demo.business.sorter.ProductSorterImpl;
import com.example.demo.business.sorter.ProductToIndex;
import com.example.demo.business.sorter.SorterResult;
import com.example.demo.dao.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
public class IndexerService {

    private final ProductRepo productRepo;

    private final ProductToIndex<Double> wordWeightIndexer = (prodTitle, wordToSearch) ->
            Optional.of (prodTitle)
                    .map (title -> wordWeightBusinessLogic (title,wordToSearch))
                    .orElseGet (() -> (double)0);

    private final ProductToIndex<Double> wordPlaceIndexer = (prodTitle,wordToSearch) ->
            Optional.of (prodTitle)
                    .map (title -> wordPlaceIndexerBusinessLogic(title, wordToSearch.split (" ")[0]))
                    .orElseGet (() -> (double)0);

    public IndexerService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }



    public List<Product> index(String wordToSearch){
        wordToSearch = wordToSearch.toUpperCase(Locale.ROOT);
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
                .orElse (-1);

        if(index == -1 && wordToSearch.split (" ").length > 1){
           return Arrays.stream (wordToSearch.split (" "))
                    .mapToDouble (tkn -> wordPlaceIndexerBusinessLogic (title,tkn))
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

}
