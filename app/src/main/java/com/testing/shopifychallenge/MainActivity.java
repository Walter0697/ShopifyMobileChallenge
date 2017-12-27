package com.testing.shopifychallenge;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//icon from Flaticon designed by Smashicons, roundicons


public class MainActivity extends AppCompatActivity {

    //product list for the list view
    ProductList items;

    //create listview and adapter for the list of products
    ListView itemsList;
    ProductAdapter pAdapter;

    //edit text for the search screen
    EditText searchText;

    //storing the page number
    int page;
    TextView pageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the page number
        page = 1;
        pageView = (TextView) findViewById(R.id.pageView);
        pageView.setText(Integer.toString(page));

        //set up context for the image process
        ImageProcess.context = getApplicationContext();

        //set up the data structure
        items = new ProductList();

        //setting the list view to the adapter
        itemsList = (ListView)findViewById(R.id.productList);
        pAdapter = new ProductAdapter(this, items);
        itemsList.setAdapter(pAdapter);

        //set up the on click listener for the list view
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                detailIntent.putExtra("PRODUCT", items.getProduct(i));
                startActivity(detailIntent);
            }
        });

        //generate the data from the api
        generateProduct();

        //set up the search functionality
        searchText = (EditText) findViewById(R.id.searchText);
        searchText.setText("");
        //search by pressing enter
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    searchFunction();
                    return true;
                }
                return false;
            }
        });
        //search by clicking button
        ImageView searchButton = (ImageView) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFunction();
            }
        });

        //set up the paging functionality
        ImageView nextPage = (ImageView) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the current list is nothing
                if (pAdapter.getCount() != 0) {
                    page++;
                    pageView.setText(Integer.toString(page));
                    generateProduct();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Cannot go further", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView previousPage = (ImageView) findViewById(R.id.previousPage);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the current is the first page
                if (page != 1) {
                    page--;
                    pageView.setText(Integer.toString(page));
                    generateProduct();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Cannot go further", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //hide the keyboard in the very beginning
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);
        layout.requestFocus();

        //hide the keyboard when not searching
        layout.setOnTouchListener(virtualKeyboardListener);
        itemsList.setOnTouchListener(virtualKeyboardListener);
        searchButton.setOnTouchListener(virtualKeyboardListener);
        nextPage.setOnTouchListener(virtualKeyboardListener);
        previousPage.setOnTouchListener(virtualKeyboardListener);
    }

    private void searchFunction()
    {
        //getting the keyword from the edittext
        String keyword = searchText.getText().toString();

        //starting the activity
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        searchIntent.putExtra("SEARCH_KEYWORD", keyword);
        startActivity(searchIntent);
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
        pAdapter.refreshEvents(items);
        pAdapter.notifyDataSetChanged();
        itemsList.invalidate();
    }

    //hide the virtual keyboard
    protected void hideKeyboard(View view)
    {
        InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //generate the product according to the page
    private void generateProduct()
    {
        //clear the list first
        items.clear();
        updateList();

        ProductGenerate.generateProducts(new ProductCallback() {
            @Override
            public void onSuccess(Product product) {
                items.addProduct(product);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateList();
                    }
                });
            }
        }, page);
    }
}


