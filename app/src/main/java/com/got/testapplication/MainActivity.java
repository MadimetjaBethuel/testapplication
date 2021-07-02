package com.got.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.got.testapplication.ml.RAmodel2;
import com.got.testapplication.ml.TextClassificationV2;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.io.InputStream;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TextClassificationDemo";

    private TextView inputText, outputText;
    private Button testButton;


    Client client;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputTest);
        outputText = findViewById(R.id.outtext);

        testButton = findViewById(R.id.testButton);

        client= new Client(getApplicationContext());
        handler = new Handler();



        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = "";
                AssetManager assetManager = getApplicationContext().getAssets();

                try {

                    InputStream inputStream = assetManager.open("AnnaKarenina.txt");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    string = new String(buffer);
                }catch (IOException e){
                    e.printStackTrace();
                }

               // java.lang.AssertionError: Error occurred when initializing NLClassifier: Type mismatch for input tensor serving_default_dense_input:0. Requested STRING, got FLOAT32.
               

              classify(string);



/*
    #Error : The size of byte buffer and the shape do not match.
                try {
                    RAmodel2 model = RAmodel2.newInstance(getApplicationContext());
                    String text = inputText.getText().toString();

                    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(string);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1}, DataType.FLOAT32);
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    RAmodel2.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();
                    String out = String.valueOf(outputFeature0.getBuffer());
                    outputText.setText(out);
                } catch (IOException e) {
                    // TODO Handle the exception
                }

 */


/*

    #Error IN32 not supported
                try {
                    TextClassificationV2 model = TextClassificationV2.newInstance(getApplicationContext());
                    String text = inputText.getText().toString();

                    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(text);
                    // Creates inputs for reference.
                    TensorBuffer inputText = TensorBuffer.createFixedSize(new int[]{1, 256}, DataType.FLOAT32);
                    inputText.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    TextClassificationV2.Outputs outputs = model.process(inputText);
                    TensorBuffer probability = outputs.getProbabilityAsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();
                    String reee=String.valueOf(probability.getFloatArray()[0]);
                    String reeea=String.valueOf(probability.getFloatArray()[1]);
                    outputText.setText(reee +""+ reeea);
               // TODO Handle the exception
                } catch (IOException e) {
                    e.printStackTrace();
                }


*/

            }




        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                  client.load();
                });
    }
    private void classify(final String text) {
        handler.post(
                () -> {
                    List<Result> results = client.classify(text);


                    showResult(results);
                });
    }
    private void showResult(final List<Result> results) {

        runOnUiThread(
                () -> {
                    String textToShow = "\nOutput:\n";
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow += String.format(result.getTitle() + (result.getConfidence()));

                        textToShow += "--------\n";

                        outputText.setText(textToShow);
                        // Extract confidence
                    }
                });


    }
}