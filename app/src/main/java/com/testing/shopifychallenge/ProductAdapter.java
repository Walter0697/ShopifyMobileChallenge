package com.testing.shopifychallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by walter on 2017-12-24.
 */

public class ProductAdapter extends BaseAdapter {

    //layout inflater
    LayoutInflater layoutInflater;
    ProductList items;

    //constructor
    public ProductAdapter(Context context, ProductList items)
    {
        //product information
        this.items = items;
        //get the layout so that it can be matched with
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //refresh the event
    public void refreshEvents(ProductList events)
    {
        items = events;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.getSize();
    }

    @Override
    public Object getItem(int i) {
        return items.getProduct(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //inflate the relative layout to show each product
        View v = layoutInflater.inflate(R.layout.list_item_detail, null);

        //setting the product information
        TextView itemName = (TextView)v.findViewById(R.id.productName);
        itemName.setText(items.getProduct(i).getTitle());

        TextView itemDescription = (TextView)v.findViewById(R.id.productDescription);
        itemDescription.setText(items.getProduct(i).getDescription());

        TextView itemPrice = (TextView)v.findViewById(R.id.productPrice);
        itemPrice.setText("$" + items.getProduct(i).getPrice());

        TextView itemVendor = (TextView)v.findViewById(R.id.productVendor);
        itemVendor.setText(items.getProduct(i).getVendor());

        //setting the image of the product
        final ImageView itemImage = (ImageView)v.findViewById(R.id.productImage);
        items.getProduct(i).getImage(new ImageCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                itemImage.setImageBitmap(bitmap);
            }
        }, 0.1f);
        return v;
    }
}
