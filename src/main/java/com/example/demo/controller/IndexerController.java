package com.example.demo.controller;

import com.example.demo.business.product.Product;
import com.example.demo.service.IndexerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1")
public class IndexerController {

    private final IndexerService service;

    public IndexerController(IndexerService service) {
        this.service = service;
    }

    @GetMapping("/index")
    public ResponseEntity<List<Product>> indexerMethod(@RequestParam("word") String wordToSearch){
        return ResponseEntity.ok(service.index(wordToSearch));
    }
}
