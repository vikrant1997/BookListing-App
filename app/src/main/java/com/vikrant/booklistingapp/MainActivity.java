package com.vikrant.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    public static final String SEARCH_URL= "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String BOOKS_REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20";
    public static  String FINAL_URL;

    private static  int BOOK_LOADER_ID;
    private BookAdapter mAdapter;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText toSearch=findViewById(R.id.Search_show);
        String search=toSearch.getText().toString();

       if(search.isEmpty()||search==null){
             FINAL_URL=BOOKS_REQUEST_URL;
             BOOK_LOADER_ID=1;

        }else{
           FINAL_URL=SEARCH_URL+search;
           BOOK_LOADER_ID=2;
       }
        Log.i(LOG_TAG,FINAL_URL);

        GridView booksView=findViewById(R.id.list);


        mEmptyStateTextView = findViewById(R.id.empty_view);
        booksView.setEmptyView(mEmptyStateTextView);

        mAdapter=new BookAdapter(this,new ArrayList<Book>());
        booksView.setAdapter(mAdapter);
        Log.i(LOG_TAG,"selector called");
        booksView.setSelector(R.drawable.listselector);

        setConnectivity();

        Button SearchButton=findViewById(R.id.Search_Button);
        SearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                EditText searchEditText=findViewById(R.id.Search_show);
                View loadingIndicator = findViewById(R.id.loading_indicator);
                mAdapter.clear();
                mEmptyStateTextView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.VISIBLE);
                String forcesearch=searchEditText.getText().toString();
                    if(!forcesearch.isEmpty()){
                        FINAL_URL=SEARCH_URL+forcesearch;
                        getLoaderManager().restartLoader(2,null,MainActivity.this);


                    }else{
                        getLoaderManager().restartLoader(1,null,MainActivity.this);
                    }

            }
        });
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        if(i==1){
            Log.i("FINAL_URI_PROCESSED",FINAL_URL);
            return new BookLoader(this,FINAL_URL);
        }
        else{
           // EditText search=findViewById(R.id.Search_show);
              //  FINAL_URL=SEARCH_URL+search.getText().toString();
            Log.i("FINAL_URI_PROCESSED",FINAL_URL);
                return new BookLoader(this,FINAL_URL);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
       View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_books);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    public void setConnectivity(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

}