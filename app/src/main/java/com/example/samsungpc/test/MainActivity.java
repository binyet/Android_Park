package com.example.samsungpc.test;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends Activity {

    public Button button;
    public int CurrentColor = 0;
    public EditText input, show;
    public Button send;
    public OutputStream os;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        send = (Button) findViewById(R.id.send);
        show = (EditText) findViewById(R.id.show);



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                button.setBackgroundColor(Color.parseColor("#FF0000"));
//                setContentView(R.layout.new_window);
                Intent intent =new Intent(MainActivity.this, NewWindow.class);
                startActivity(intent);

            }
        });
    }

}
