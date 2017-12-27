package com.testing.shopifychallenge;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by walter on 2017-12-24.
 */

public class ProductGenerate {

    //generate the products to the list from the api
    public static void generateProducts(final ProductCallback callback, int page)
    {
        //set up client
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                                .url(getAPIAddress(page))
                                .build();

        //send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected Code " + response);
                } else {
                    String jsonData = response.body().string();
                    Log.d("result", jsonData);
                    try {
                        //parsing the data into a JSONarray first
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        //sending the product to the list one by one
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            //getting the current product
                            JSONObject currentProduct = jsonArray.getJSONObject(i);
                            
                            callback.onSuccess(createProduct(currentProduct));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //generate search results with a keyword
    public static void searchProducts(final String keyword, final ProductCallback callback, final int page)
    {
        //set up a client
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(getAPIAddress(page))
                .build();

        //send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected Code " + response);
                } else {
                    String jsonData = response.body().string();
                    try{
                        //parsing the data into a JSONarray first
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        //sending the product to the list one by one
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            //getting the current product
                            JSONObject currentProduct = jsonArray.getJSONObject(i);
                            Product product = createProduct(currentProduct);

                            //send it to the list if it is a match
                            if (product.isMatch(keyword))
                                callback.onSuccess(product);
                        }

                        //call the function again for the next page
                        if (jsonArray.length() != 0)
                            searchProducts(keyword, callback, page+1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //create a product with jsondata
    private static Product createProduct(JSONObject jsonData)
    {
        try {
            //parsing the data
            return new Product(jsonData.getLong("id")
                    , jsonData.getString("title")
                    , jsonData.getString("body_html")
                    , jsonData.getJSONArray("images").getJSONObject(0).getString("src")
                    , jsonData.getString("vendor")
                    , jsonData.getString("product_type")
                    , jsonData.getString("handle")
                    , jsonData.getString("created_at")
                    , jsonData.getString("updated_at")
                    , jsonData.getString("published_scope")
                    , jsonData.getString("tags")
                    , jsonData.getJSONArray("variants").getJSONObject(0).getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //getting api address according to the page
    //it is not the requirement of the challenge but I want to do it anyway
    private static String getAPIAddress(int page)
    {
        return "https://shopicruit.myshopify.com/admin/products.json?page="+page+"&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    }
}
