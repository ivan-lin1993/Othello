package com.ivan.othello;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button restart_btn;
    private Button regrete_btn;
    private MainBoard mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        restart_btn=(Button)findViewById(R.id.restart_btn);
        regrete_btn=(Button)findViewById(R.id.regret_btn);
        mb=(MainBoard)findViewById(R.id.mainboard);
        mb.setContext(this);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mb.Restart();
            }
        });
        regrete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mb.Regret();
            }
        });
    }
    @Override
     public boolean onTouchEvent(MotionEvent event){
        float a=0,b=0;
        mb.TouchFunc(event, a, b);
        return true;
    }

}
