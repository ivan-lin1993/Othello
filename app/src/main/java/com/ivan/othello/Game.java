package com.ivan.othello;

import android.content.Context;
import android.widget.Toast;

import com.ivan.othello.test.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 2016/6/3.
 */
public class Game {
    private int cellnum=64;
    private boolean nowPresentB=true;
    private char []table=new char[cellnum];
    private boolean gameover=false;
    private Context mainContext;

    private GameLog gameLog;

    Game(Context context){
        mainContext=context;
        GameInitial();
    }

    public void GameInitial(){
        gameLog=new GameLog();
        nowPresentB=true;
        for (int i=0;i<cellnum;i++){
            table[i]=' ';
        }
        gameover=false;
//        table[0]='W';
//        table[1]='W';
//        table[8]='W';
//        table[9]='B';
//        table[27]='B';
//        table[32]='W';
//        table[40]='B';


        table[27]='W';
        table[36]='W';
        table[28]='B';
        table[35]='B';
//        Table _table=new Table();
//        _table.table4(table);

        //gameReverse Record
        gameLog.pushTableToList(table.clone());
        gameLog.pushSideToList(nowPresentB);
    }
    public char[] getTable(){
        return table.clone();
    }
    private boolean setAble(int ind){
        List list=GameRule.getSetableList(table,nowPresentCHAR());
        for (int i=0;i<list.size();i++){
            if(ind==(int)list.get(i)){
                return true;
            }
        }
        return false;
    }
    public void play(int ind){
        if(!gameover){
            if(setAble(ind)){
                //反悔
                gameLog.pushTableToList(table.clone());
                gameLog.pushSideToList(nowPresentB);


                if(nowPresentB) table[ind]='B';
                else table[ind]='W';
                table=GameRule.Reverse(table.clone(),ind,nowPresentCHAR());
                Turn();
            }
        }
        if(GameRule.getSetableList(getTable(),nowPresentCHAR()).size()==0){
            Turn();
            if(GameRule.getSetableList(getTable(),nowPresentCHAR()).size()==0){
                return;
            }
            else Toast.makeText(mainContext,"沒有可下的區域",Toast.LENGTH_SHORT).show();
        }
    }
    public void Regret(){
        if(gameLog.isPopAble()){
            table=gameLog.popTableFromList();
            nowPresentB=gameLog.popSideFromList();
            Toast.makeText(mainContext,"Regret!",Toast.LENGTH_SHORT).show();
            gameover=false;
        }
    }
    public int showScore(char color){
        int count=0;
        for (int i=0;i<64;i++){
            if(table[i]==color){
                count++;
            }
        }
        return count;
    }
    public int showScore(char color,char []table){
        int count=0;
        for (int i=0;i<64;i++){
            if(table[i]==color){
                count++;
            }
        }
        return count;
    }
    private void Turn(){
        nowPresentB=!nowPresentB;
    }
    public char nowPresentCHAR(){
        if(nowPresentB) return 'B';
        else return 'W';
    }

}
