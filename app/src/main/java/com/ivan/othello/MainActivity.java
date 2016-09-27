package com.ivan.othello;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ivan.othello.ai.AI;
import com.ivan.othello.ai.AI_greedy;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private MainBoard mb;
    private Context thisContext;
    private TextView result_tv, state_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        thisContext = this;
        Button restart_btn = (Button) findViewById(R.id.restart_btn);
        Button regrete_btn = (Button) findViewById(R.id.regret_btn);
        result_tv = (TextView) findViewById(R.id.result_tv);
        state_tv = (TextView) findViewById(R.id.state_tv);

        game = new Game(this);
        mb = (MainBoard) findViewById(R.id.mainboard);
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game = new Game(thisContext);
                mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
                showText();
            }
        });
        regrete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.Regret();
                mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
                showText();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            getScreenSize(size);
            mb.setViewDisplay(size.y, size.x);
            mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());

        } else {
            //noinspection deprecation
            Display display = getWindowManager().getDefaultDisplay();
            mb.setViewDisplay(display.getWidth(), display.getHeight());
        }
        showText();
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
                mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
                showText();
            }
        });
        alertDialog.setNegativeButton("反悔", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game.Regret();
                mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
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
            mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
            showText();
            if (game.isGameOver()) {
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

    public void AIplay(View v) {
        try {
            AI_greedy ai = new AI_greedy(thisContext, new AI.AsyncResponse() {
                @Override
                public void processFinish(Integer output) {
                    //Log.e("result",output.toString());
                    game.play(game.getSetAbleList()[output]);
                    mb.drawAllBoard(game.getTable(), game.getSetAbleList(), game.nowPresentCHAR());
                    //mb.drawPut(game.getSetAbleList()[output]);
                    showText();
                }
            });
            ai.execute(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
