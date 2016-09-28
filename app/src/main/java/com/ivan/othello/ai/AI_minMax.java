package com.ivan.othello.ai;

import android.content.Context;
import android.util.Log;

import com.ivan.othello.Game;
import com.ivan.othello.GameRule;

import java.util.List;

/**
 * Created by Ivan Lin on 2016/9/27.
 */
public class AI_minMax extends AI {
    private char Max_color='B',min_color='W';
    private int result_ind=0;
    private int search_dept=3;
    private class Node{
        public int dept,score,put;
        public Node parent=null;
        public Node(){};
        public Node(int d,int s,int put,Node p){
            dept=d;
            score=s;
            this.put=put;
            parent=p;
        }
    }

    public AI_minMax(Context context, AsyncResponse delegate) {
        super(context, delegate);
    }
    public void setDept(int search_dept){
        this.search_dept=search_dept;
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
                if(g.nowPresentCHAR()=='W'){
                    Max_color='W';
                    min_color='B';
                }
                Node parent=new Node(2,3,-1,null);
                parent.dept=search_dept;
                Node _result=minMaxValue(g.getTable(),parent,-999,999,true);
                Log.e("minMax:score",String.valueOf(_result.score));
                while(_result.parent!=null){
                    result_ind=_result.put;
                    _result=_result.parent;
                }
                Log.e("minMax:result",String.valueOf(result));
                Log.e("minMax:score",String.valueOf(_result.score));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("minMax:resultid",String.valueOf(result_ind));
        return result_ind;
    }
    private Node minMaxValue(char []table,Node parent,int alpha,int beta,boolean isMax){
        Log.e("minMax:a b",String.valueOf(alpha)+" ,"+String.valueOf(beta));
        Log.e("minMax:dept",String.valueOf(parent.dept));
        Log.e("minMax:table",String.valueOf(table));
        if(parent.dept==0|| GameRule.isGameOver(table)){
            return parent;
        }
        else if(isMax){
            Log.e("minMax","isMax");
            char []temp_table=table.clone();
            List <Integer>list =GameRule.getSetableList(table,Max_color);
            Node best;
            if(list.size()==0){
                best=new Node(parent.dept-1,-1,-1,parent);
            }
            else {
                best=new Node(parent.dept - 1, -1, list.get(0), parent);
            }
            for(int i:list){
                char []childTable=GameRule.Reverse(temp_table.clone(),i,Max_color);
                Log.e("minMax:child",String.valueOf(childTable));
                childTable[i]=Max_color;
                Log.e("minMax:child",String.valueOf(childTable));
                int score= GameRule.showScore(childTable,Max_color);
                Node childnode=new Node(parent.dept-1,score,i,parent);
                Node v=minMaxValue(childTable,childnode,alpha,beta,false);
                if(v.score>best.score){
                    best=v;
                }
                alpha=(best.score>alpha)?best.score:alpha;
                if(beta<=alpha){
                    Log.e("minMax:puring","beta puring");
                    break;
                }
            }
            Log.e("minMax:score",String.valueOf(best.score));
            Log.e("minMax:put",String.valueOf(best.put));
            return best;
        }
        else {
            Log.e("minMax","ismin");
            char []temp_table=table.clone();
            List<Integer> list = GameRule.getSetableList(table, min_color);
            Node best;
            if(list.size()==0){
                best=new Node(parent.dept-1,64,-1,parent);
            }
            else {
                best=new Node(parent.dept - 1, -1, list.get(0), parent);
            }
            for (int i : list) {
                char []childTable=GameRule.Reverse(temp_table.clone(),i,min_color);
                Log.e("minMax:child",String.valueOf(childTable));
                childTable[i]=min_color;
                Log.e("minMax:child",String.valueOf(childTable));

                int score= GameRule.showScore(childTable,Max_color);
                Node childnode=new Node(parent.dept-1,score,i,parent);
                Node v=minMaxValue(childTable,childnode,alpha,beta,true);
                if(v.score<best.score){
                    best=v;
                }
                beta=(best.score<beta)?best.score:beta;
                if(beta<=alpha){
                    Log.e("minMax:puring","alpha puring");
                    break;
                }
            }
            Log.e("minMax:score",String.valueOf(best.score));
            Log.e("minMax:put",String.valueOf(best.put));
            return best;
        }
    }
}
