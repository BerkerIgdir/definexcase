package com.example.demo.dao;

import com.example.demo.business.enums.CategoryLevel;
import com.example.demo.business.product.Category;
import com.example.demo.business.product.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepo {
    private static final List<Product> productList = Collections.synchronizedList(new ArrayList<>());

    static{
        var accessoryCategory = new Category(CategoryLevel.FOUR,"accessory");
        var cellPhoneCategory = new Category(CategoryLevel.TWO,"cellphone");
        var product1 = new Product(1L,"My Iphone Spingen Mavi Pro Max Kilif",accessoryCategory,true);
        var product2 = new Product(2L,"IPhone 11 128 GB Cep telefonu",cellPhoneCategory,true);
        var product3 = new Product(3L,"IPhone 11 Pro Max Silikon Siyah Kilif",accessoryCategory,true);
        var product4 = new Product(4L,"Blackberry 11 Spingen Mavi Pro Max Kilif",accessoryCategory,true);
        var product5 = new Product(5L,"Blue Iphone Spingen Mavi Pro Max Kilif",accessoryCategory,false);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        productList.add(product5);
    }

    public List<Product> getAllProducts(){
        return productList.stream()
                .filter(Product::isActive)
                .collect(Collectors.toUnmodifiableList());
    }
}
