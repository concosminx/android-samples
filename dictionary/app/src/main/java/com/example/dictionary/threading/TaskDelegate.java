package com.example.dictionary.threading;

import com.example.dictionary.models.Word;

import java.util.ArrayList;

public interface TaskDelegate {
    void onWordsRetrieved(ArrayList<Word> words);

    void onRowsRetrieved(int numRows);
}
