package com.example.smartmobilitypro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private static String message;
    private static StringBuilder recDataString = new StringBuilder();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EscolhaModal.class);
                startActivity(intent);
            }
        });
    }

    public static void setMessage(String newMessage){

        message = newMessage;
        recDataString.append(message);      								//keep appending to string until ~
        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
        if (endOfLineIndex > 0) {                                           // make sure there data before ~
            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
            Log.e(TAG, dataInPrint);
            recDataString.delete(0, recDataString.length());                    //clear all string data
        }


    }

}
