package com.ivan.othello;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ivan.othello.ai.AI;
import com.ivan.othello.ai.AI_greedy;
import com.ivan.othello.ai.AI_minMax;
import com.ivan.othello.test.minMaxTest;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private MainBoard mb;
    private Context thisContext;
    private TextView result_tv, state_tv;
    private int ai_dept=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        thisContext = this;
        Button restart_btn = (Button) findViewById(R.id.restart_btn);
        Button regrete_btn = (Button) findViewById(R.id.regret_btn);
        Button AI_btn=(Button)findViewById(R.id.ai_play_btn);
        Button setDept_btn=(Button)findViewById(R.id.setDept_btn);
        result_tv = (TextView) findViewById(R.id.result_tv);
        state_tv = (TextView) findViewById(R.id.state_tv);

        game = new Game(this);
        mb = (MainBoard) findViewById(R.id.mainboard);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = new Game(thisContext);
                mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
                showText();
            }
        });
        regrete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.Regret();
                mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
                showText();
            }
        });
        AI_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AIplay();
            }
        });
        setDept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAi_dept();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            getScreenSize(size);
            mb.setViewDisplay(size.y, size.x);
            mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());

        } else {
            //noinspection deprecation
            Display display = getWindowManager().getDefaultDisplay();
            mb.setViewDisplay(display.getWidth(), display.getHeight());
        }
        showText();

//        test
//
//        minMaxTest minMaxTest=new minMaxTest();
//        com.ivan.othello.test.minMaxTest.Node node=minMaxTest.MinMax(minMaxTest.root,-999,999,true);
//        Log.e("test End","dept:"+node.dept+" num:"+node.num+" value:"+node.value);
    }

    private Display getScreenSize(Point size) {
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return display;
    }

    private void GameOverDialog() {
        int w = game.showScore('W');
        int b = game.showScore('B');

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(thisContext);
        if (w > b) alertDialog.setTitle("白子獲勝!!");
        else if (w < b) alertDialog.setTitle("黑子獲勝!!");
        else alertDialog.setTitle("平手!!");

        alertDialog.setMessage("黑子:" + game.showScore('B') + " 白子:" + game.showScore('W'));
        alertDialog.setPositiveButton("再來一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game = new Game(thisContext);
                mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
                showText();
            }
        });
        alertDialog.setNegativeButton("反悔", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game.Regret();
                mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
                showText();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int setID = mb.TouchFunc(event);
            game.play(setID);
            mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
            showText();
            if (GameRule.isGameOver(game.getTable())) {
                GameOverDialog();
            }
        }
        return true;
    }

    private void showText() {
        result_tv.setText("黑子:" + game.showScore('B') + " 白子:" + game.showScore('W'));
        if (game.nowPresentCHAR() == 'B') state_tv.setText("目前:黑");
        else state_tv.setText("目前:白");
    }
    public void setAi_dept(){
        AlertDialog.Builder setDept=new AlertDialog.Builder(thisContext);

        setDept.setTitle("調整深度");
        final EditText editText=new EditText(thisContext);
        editText.setHint(String.valueOf(ai_dept));
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setDept.setView(editText);
        setDept.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ai_dept=Integer.parseInt(editText.getText().toString());
            }
        });
        setDept.setNegativeButton("取消",null);
        setDept.show();
    }
    public void AIplay() {
        try {
            AI_minMax ai = new AI_minMax(thisContext, new AI.AsyncResponse() {
                @Override
                public void processFinish(Integer output) {
                    //Log.e("result",output.toString());
                    //game.play(GameRule.getSetableList(game.getTable(),game.nowPresentCHAR()).get(output));
                    game.play(output);
                    mb.drawAllBoard(game.getTable(), game.nowPresentCHAR());
                    mb.drawPut(output);
                    showText();
                }
            });
            ai.setDept(ai_dept);
            ai.execute(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
