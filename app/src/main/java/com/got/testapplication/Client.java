package com.got.testapplication;


import android.content.Context;
import android.util.Log;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {
    private static final String TAG = "TaskApi";
    private static final String MODEL_PATH = "RAmodel2.tflite";

    private final Context context;

   NLClassifier classifier;
    NLClassifier.NLClassifierOptions
            options = NLClassifier.NLClassifierOptions.builder().setInputTensorName("0").setOutputScoreTensorName("0").build();

    public Client(Context context) {
        this.context = context;
    }

    public void load() {
        try {
            classifier = NLClassifier.createFromFile(context, MODEL_PATH);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void unload() {
        classifier.close();
        classifier = null;
    }

    public List<Result> classify(String text) {
        List<Category> apiResults = classifier.classify(text);
        List<Result> results = new ArrayList<>(apiResults.size());
        for (int i = 0; i < apiResults.size(); i++) {
            Category category = apiResults.get(i);
            results.add(new Result("" + i, category.getLabel(), category.getScore()));
        }
        Collections.sort(results);
        return results;
    }

}
