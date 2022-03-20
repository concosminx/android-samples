package com.example.dictionary;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dictionary.adapters.WordsRecyclerAdapter;
import com.example.dictionary.models.Word;
import com.example.dictionary.threading.DeleteWordAsyncTask;
import com.example.dictionary.threading.RetrieveWordsAsyncTask;
import com.example.dictionary.threading.TaskDelegate;
import com.example.dictionary.util.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class DictionaryAsyncTasksActivity extends AppCompatActivity implements
        WordsRecyclerAdapter.OnWordListener,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        TaskDelegate {

    private static final String TAG = "DictionaryActivity";

    //ui components
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    //vars
    private ArrayList<Word> mWords = new ArrayList<>();
    private WordsRecyclerAdapter mWordRecyclerAdapter;
    private FloatingActionButton mFab;
    private String mSearchQuery = "";
    private RetrieveWordsAsyncTask mRetrieveWordsAsyncTask;
    private DeleteWordAsyncTask mDeleteWordAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstanceState(savedInstanceState);

        setContentView(R.layout.activity_dictionary);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFab = findViewById(R.id.fab);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);

        mFab.setOnClickListener(this);
        mSwipeRefresh.setOnRefreshListener(this);

        setupRecyclerView();
    }


    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mWords = savedInstanceState.getParcelableArrayList("words");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("words", mWords);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called.");
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: called.");
        super.onStop();
        if (mRetrieveWordsAsyncTask != null) {
            mRetrieveWordsAsyncTask.cancel(true);
        }
        if (mDeleteWordAsyncTask != null) {
            mDeleteWordAsyncTask.cancel(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSearchQuery.length() > 2) {
            onRefresh();
        }
    }

    private void retrieveWords(String title) {
        Log.d(TAG, "retrieveWords: called.");

        if(mRetrieveWordsAsyncTask != null){
            mRetrieveWordsAsyncTask.cancel(true);
        }
        mRetrieveWordsAsyncTask = new RetrieveWordsAsyncTask(this, this);
        mRetrieveWordsAsyncTask.execute(title);
    }


    public void deleteWord(Word word) {
        Log.d(TAG, "deleteWord: called.");
        mWords.remove(word);
        mWordRecyclerAdapter.getFilteredWords().remove(word);
        mWordRecyclerAdapter.notifyDataSetChanged();

        if(mDeleteWordAsyncTask != null){
            mDeleteWordAsyncTask.cancel(true);
        }
        mDeleteWordAsyncTask = new DeleteWordAsyncTask(this);
        mDeleteWordAsyncTask.execute(word);
    }


    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: called.");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mWordRecyclerAdapter = new WordsRecyclerAdapter(mWords, this);
        mRecyclerView.setAdapter(mWordRecyclerAdapter);
    }

    @Override
    public void onWordClick(int position) {
        Intent intent = new Intent(this, EditWordActivity.class);
        intent.putExtra("selected_word", mWords.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab: {
                Intent intent = new Intent(this, EditWordActivity.class);
                startActivity(intent);
                break;
            }

        }
    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteWord(mWords.get(mWords.indexOf(mWordRecyclerAdapter.getFilteredWords().get(viewHolder.getAdapterPosition()))));
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dictionary_activity_actions, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (query.length() > 2) {
                    mSearchQuery = query;
                    retrieveWords(mSearchQuery);
                } else {
                    clearWords();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (query.length() > 2) {
                    mSearchQuery = query;
                    retrieveWords(mSearchQuery);
                } else {
                    clearWords();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void clearWords() {
        if (mWords != null) {
            if (mWords.size() > 0) {
                mWords.clear();
            }
        }
        mWordRecyclerAdapter.getFilter().filter(mSearchQuery);
    }

    @Override
    public void onRefresh() {
        retrieveWords(mSearchQuery);
        mSwipeRefresh.setRefreshing(false);
    }


    @Override
    public void onWordsRetrieved(ArrayList<Word> words) {
        clearWords();

        mWords.addAll(words);
        mWordRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRowsRetrieved(int numRows) {

    }
}



