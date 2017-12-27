package com.testing.shopifychallenge;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import co.lujun.androidtagview.TagContainerLayout;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getIntent().hasExtra("PRODUCT"))
        {
            product = (Product) getIntent().getSerializableExtra("PRODUCT");
        }
        else
        {
            Log.d("error", "no product found");
        }

        //set up image for the product
        final ImageView productImage = (ImageView) findViewById(R.id.detailImage);
        product.getImage(new ImageCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                productImage.setImageBitmap(bitmap);
            }
        }, 0.15f);

        //set up every information for the product
        TextView productTitle = (TextView) findViewById(R.id.detailTitle);
        productTitle.setText(product.getTitle());

        TextView productVendor = (TextView) findViewById(R.id.detailVendor);
        productVendor.setText("from " + product.getVendor());

        TextView productPrice = (TextView) findViewById(R.id.detailPrice);
        productPrice.setText("$" + product.getPrice());

        TextView productDescription = (TextView) findViewById(R.id.detailDescription);
        productDescription.setText(product.getDescription());

        TextView productType = (TextView) findViewById(R.id.detailType);
        productType.setText("Type: " + product.getProductType());

        TextView productPublishedGroup = (TextView) findViewById(R.id.detailPublishedGroup);
        productPublishedGroup.setText("Published Group: " + product.getPublished_group());

        TextView productHandle = (TextView) findViewById(R.id.detailHandle);
        productHandle.setText("Handle: " + product.getHandle());

        TagContainerLayout productTags = (TagContainerLayout) findViewById(R.id.detailTags);
        for (int i = 0; i < product.getTags().length; i++)
            productTags.addTag(product.getTags()[i]);

        TextView productCreated = (TextView) findViewById(R.id.detailCreatedAt);
        productCreated.setText("Created at " + product.getCreated_at());

        TextView productUpdated = (TextView) findViewById(R.id.detailUpdatedAt);
        productUpdated.setText("Updated at " + product.getUpdated_at());
    }
}
