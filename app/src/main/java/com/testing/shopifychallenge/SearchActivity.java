package com.testing.shopifychallenge;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    //product list for the list view
    ProductList searchItems;
    //storing the keyword for the search
    String keyword;

    //list view and adapter
    ListView searchList;
    ProductAdapter pAdapter;
    ImageView loadingImage;

    EditText searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //set up the edittext
        searchBar = (EditText) findViewById(R.id.searchText);

        //set up the data structure
        searchItems = new ProductList();

        if (getIntent().hasExtra("SEARCH_KEYWORD"))
        {
            keyword = getIntent().getStringExtra("SEARCH_KEYWORD");
            searchBar.setText(keyword);
            //generate the searched products
            ProductGenerate.searchProducts(keyword, new ProductCallback() {
                @Override
                public void onSuccess(Product product) {
                    searchItems.addProduct(product);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                        }
                    });
                }
            }, 1);
        }
        else
        {
            //should have a keyword when entering this activity
            Log.d("error", "Invalid data");
        }

        //set up list view and adapter
        searchList = (ListView) findViewById(R.id.searchList);
        pAdapter = new ProductAdapter(this, searchItems);
        searchList.setAdapter(pAdapter);

        //set up the on click listener for the list view
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                detailIntent.putExtra("PRODUCT", searchItems.getProduct(i));
                startActivity(detailIntent);
            }
        });

        //search again if the button clicked
        ImageView searchButton = (ImageView) findViewById(R.id.searchButton);
        //search by pressing enter
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchFunction();
                    return true;
                }
                return false;
            }
        });
        //search by clicking button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFunction();
            }
        });

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.searchLayout);
        layout.requestFocus();

        //set up the loading image if the list is empty
        loadingImage = (ImageView) findViewById(R.id.loadingImage);
        if (pAdapter.getCount() != 0)
            loadingImage.setVisibility(View.INVISIBLE);

        //hide the keyboard when not searching
        layout.setOnTouchListener(virtualKeyboardListener);
        searchList.setOnTouchListener(virtualKeyboardListener);
        searchButton.setOnTouchListener(virtualKeyboardListener);

        //exit button for stop searching
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void searchFunction()
    {
        String keyword = searchBar.getText().toString();
        searchItems.clear();
        loadingImage.setVisibility(View.VISIBLE);
        ProductGenerate.searchProducts(keyword, new ProductCallback() {
            @Override
            public void onSuccess(Product product) {
                searchItems.addProduct(product);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateList();
                    }
                });
            }
        }, 1);
    }

    //listener to hide the keyboard
    View.OnTouchListener virtualKeyboardListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            hideKeyboard(view);
            return false;
        }
    };

    //update the listview
    public void updateList(){
        pAdapter.refreshEvents(searchItems);
        pAdapter.notifyDataSetChanged();
        searchList.invalidate();


        //change the visibility of the loading image account to the number of the items in the list
        if (pAdapter.getCount() != 0)
            loadingImage.setVisibility(View.INVISIBLE);
        else
            loadingImage.setVisibility(View.VISIBLE);
    }

    //hide the virtual keyboard
    protected void hideKeyboard(View view)
    {
        InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
