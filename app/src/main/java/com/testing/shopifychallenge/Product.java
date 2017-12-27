package com.testing.shopifychallenge;

//set it to serializable in order to pass through intent
import android.util.Log;

import java.io.Serializable;

/**
 * Created by walter on 2017-12-24.
 */

public class Product implements Serializable{
    //store all the necessary information in this class
    private long id;
    private String title;
    private String description;
    private String imageURL;
    private String vendor;
    private String productType;
    private String handle;
    private String published_group;
    private String[] tags;
    private String price;

    //date related
    private String updated_at;
    private String created_at;

    public Product(long id, String title, String description, String imageURL, String vendor,
                   String productType, String handle, String created_at, String updated_at,
                   String published_group, String tags, String price)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.vendor = vendor;
        this.productType = productType;
        this.handle = handle;
        this.published_group = published_group;
        this.tags = separateTags(tags);
        this.price = price;

        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    //see if this product is matched with the searching keyword
    public boolean isMatch(String keyword)
    {
        String findMatch = keyword.toUpperCase();
        //see if it is substring of the title
        if (title.toUpperCase().indexOf(findMatch) != -1)
            return true;
        //see if anyone of them match the keyword
        if (vendor.toUpperCase().indexOf(findMatch) != -1)
            return true;
        if (handle.toUpperCase() == findMatch)
            return true;
        if (productType.toUpperCase() == findMatch)
            return true;
        for (int i = 0; i < tags.length; i++)
            if (tags[i].toUpperCase() == findMatch)
                return true;
        //if nothing match, return false
        return false;
    }

    //separate tags into string array
    public String[] separateTags(String tag)
    {
        return tag.split(", ");
    }

    //getter of title
    public String getTitle()
    {
        return title;
    }

    //getter of description
    public String getDescription()
    {
        return description;
    }

    //getter of vendor
    public String getVendor() { return vendor; }

    //getter of product type
    public String getProductType() { return productType; }

    //getter of handle
    public String getHandle() { return handle; }

    //getter of published group
    public String getPublished_group() { return published_group; }

    //getter of tags
    public String[] getTags() { return tags; }

    //getter of price
    public String getPrice() { return price; }

    //getter of date of creating
    public String getCreated_at() { return created_at; }

    //getter of date of updating
    public String getUpdated_at() { return updated_at; }

    //getting the image and then set it into the imageview
    public void getImage(ImageCallback callback, float ratio)
    {
        ImageProcess.getImageBitmap(imageURL, ratio, callback);
    }
}
