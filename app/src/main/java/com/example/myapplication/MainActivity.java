package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    private LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.line1);

        Button connect = (Button) findViewById(R.id.connect);
        Button disconnect = (Button) findViewById(R.id.button);
        Button sendbtn = (Button) findViewById(R.id.sendbtn);
        editText = findViewById(R.id.inputtext);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                attemptSend();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mSocket.connected()) {
                    mSocket.on("new message", onNewMessage);
                    mSocket.connect();
                }
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mSocket.disconnect();

            }
        });
    }
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.43.104:3000");

        } catch (URISyntaxException e) {}
    }


    private void attemptSend() {
        String message = editText.getText().toString();
        addTextView(message);
        mSocket.emit("message", message);
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this
                    .runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message;
                    try {
                        message = (String) args[0];
                        addTextView(message);
                        
                    } catch (ClassCastException e) {
                        return; // If casting fails, exit the function
                    }
                }
            });
        }
    };
    private void addTextView(String message) {
        TextView textView = new TextView(this);
        textView.setText(message); // Set some text
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(18); // Set text size
        textView.setPadding(8, 8, 8, 8); // Add padding
        editText.setText("");
        container.addView(textView);
    }

}