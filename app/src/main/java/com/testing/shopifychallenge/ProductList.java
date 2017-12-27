package com.testing.shopifychallenge;

import java.util.ArrayList;

/**
 * Created by walter on 2017-12-24.
 */

public class ProductList {
    //storing all the product inside this class
    private ArrayList<Product> products;

    public ProductList()
    {
        //initialize arrayList
        products = new ArrayList<>();
    }

    //clear the list
    public void clear() { products.clear(); }

    //getting the size of the list
    public int getSize()
    {
        return products.size();
    }

    //getting the product from the list
    public Product getProduct(int i)
    {
        return products.get(i);
    }

    //adding the product into the list
    public void addProduct(Product product)
    {
        products.add(product);
    }
}
