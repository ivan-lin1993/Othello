package com.ivan.othello;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

    }
    @Override
     public boolean onTouchEvent(MotionEvent event){
        float a=0,b=0;
        MainBoard mb=(MainBoard)findViewById(R.id.mainboard);

        mb.TouchFunc(event, a, b);
        return true;
    }
}
