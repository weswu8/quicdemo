package com.weswu.cloudcdn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static MainActivity instance;
    public static TextView quicResText;
    public static TextView httpsResText;
    private Button loadImageBtn;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGrabber();
    }

    private void initGrabber(){
        quicResText = (TextView) findViewById(R.id.quic_time_label);
        httpsResText = (TextView) findViewById(R.id.https_time_label);
        loadImageBtn = (Button) findViewById(R.id.images_button);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }

    public int getScreenHeight(){
        return  displayMetrics.heightPixels;
    }

    public int getScreenWidth(){
        return  displayMetrics.widthPixels;
    }

    public void onClickLoadImages(View view) {
        Stats.reset();
        //((Button) view).setEnabled(false);
        quicResText.setText("Loading via quic ......");
        //final GridView2 quicGv = (GridView2) findViewById(R.id.quic_gv);
        //quicGv.setAdapter(new QuicGvAdapter(this));
        httpsResText.setText("Loading via https ......");
        //final GridView2 httpsGv = (GridView2) findViewById(R.id.https_gv);
        //httpsGv.setAdapter(new HttpsGvAdapter(this));
//        runOnUiThread(new Runnable() {
//            public void run() {
//                final GridView2 quicGv = (GridView2) findViewById(R.id.quic_gv);
//                quicGv.setAdapter(new QuicGvAdapter(getParent()));
//            }
//        });
        Handler quicHandler = new Handler();
        quicHandler.post(new Runnable() {
            @Override
            public void run() {
                final GridView2 quicGv = (GridView2) findViewById(R.id.quic_gv);
                quicGv.setAdapter(new QuicGvAdapter(getApplicationContext()));
            }
        });
        Handler httpsHandler = new Handler();
        httpsHandler.post(new Runnable() {
            @Override
            public void run() {
                final GridView2 httpsGv = (GridView2) findViewById(R.id.https_gv);
                httpsGv.setAdapter(new HttpsGvAdapter(getBaseContext()));
            }
        });

//        runOnUiThread(new Runnable() {
//            public void run() {
//                final GridView2 httpsGv = (GridView2) findViewById(R.id.https_gv);
//                httpsGv.setAdapter(new HttpsGvAdapter(getParent()));
//            }
//        });
    }


}
