package com.ivan.othello.ai;

import android.content.Context;
import android.util.Log;

import com.ivan.othello.Game;

/**
 * Created by Ivan Lin on 2016/9/27.
 */
public class AI_minMax extends AI {
    private Game game;

    public AI_minMax(Context context, AsyncResponse delegate) {
        super(context, delegate);
    }
    @Override
    protected void onPostExecute(Integer _result) {
        super.onPostExecute(_result);
        delegate.processFinish(_result);
    }
    @Override
    protected Integer doInBackground(Game... params) {
        try{
            for (Game g:params) {
                game=g;
                //result=greedy_alg(g);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("greed",String.valueOf(result));
        return result;
    }
    private void minMaxValue(char []table,int dept,boolean isMax){
//        if(dept==0||game.isGameOver()){
//            //return game.showScore()
//
//        }
    }

}
