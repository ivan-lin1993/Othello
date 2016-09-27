package com.ivan.othello.ai;

import android.content.Context;
import android.util.Log;

import com.ivan.othello.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan Lin on 2016/9/27.
 */
public class AI_greedy extends AI {
    public AI_greedy(Context context, AsyncResponse delegate) {
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
                result=greedy_alg(g);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("greed",String.valueOf(result));
        return result;
    }
    private int greedy_alg(Game game){
        char[] origin_table=game.getTable();
        char now_present=game.nowPresentCHAR();

        List<Integer> position=new ArrayList<Integer>();
        List<Integer> score=new ArrayList<Integer>();
        for(int i:game.getSetAbleList()){
            position.add(i);
        }
        for(int i:position){

            char []temp=game.Reverse(i,now_present,origin_table.clone());
            temp[i]=now_present;
            Log.e("greedy",String.valueOf(temp));
            score.add(game.showScore(now_present,temp));
        }
        Log.e("greedy",score.toString());
        return chooseMax(score);
    }
    private int chooseMax(List<Integer> list){
        List<Integer> max_list=new ArrayList<>();
        int max=list.get(0);
        for (int i=0;i<list.size();i++){
            if(list.get(i)==max){
                max_list.add(i);
            }
            else if (list.get(i)>max){
                max_list.clear();
                max_list.add(i);
                max=list.get(i);
            }
        }

        int random=max_list.get((int) (Math.random() * max_list.size()));
        Log.e("random",String.valueOf(max_list));
        Log.e("random", String.valueOf(random));
        return random;
    }
}
