package com.ivan.othello;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Ivan on 2016/6/3.
 */
public class Game {
    private char win=' ';
    private int cellnum=64;
    private boolean nowPresentB=true;
    private char []table=new char[cellnum];
    private boolean gameover=false;
    private int[] direct={-8,-7,1,9,8,7,-1,-9};
    private int total_direct_num=8;
    private Context mainContext;

    private List<Integer> setAble = new ArrayList<>();
    private GameLog gameLog;
    private int ind;

    Game(Context context){
        mainContext=context;
        GameInitial();
    }

    public void GameInitial(){
        gameLog=new GameLog();
        win=' ';
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
        gameLog.pushTableToList(table.clone());
        gameLog.pushSideToList(nowPresentB);
        SetableList();
    }
    public boolean isGameOver(){
        return gameover;

    }
    public char[] getTable(){
        return table;
    }
    boolean isFull(){
        for (int i=0;i<cellnum;i++){
            if(table[i]==' ') return false;
        }
        return true;
    }

    private char whoWin(){
        int w=0,b=0;
        for(int i=0;i<cellnum;i++){
            if(table[i]=='W')w++;
            else if(table[i]=='B')b++;
        }
        if(w>b) return 'W';
        else if (w<b) return 'B';
        else return 'D';
    }
    private boolean isInBoard(int ind){
        return ind<64&&ind>=0;
    }
    private boolean isWall(int ind){
        int[] wall={0,1,2,3,4,5,6,7,8,15,16,23,24,31,32,39,40,47,48,55,56,57,58,59,60,61,62,63};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    private boolean isUpWall(int ind){
        int[] wall={0,1,2,3,4,5,6,7};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    private boolean isDownWall(int ind){
        int[] wall={56,57,58,59,60,61,62,63};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    private boolean isRightWall(int ind){
        int[] wall={7,15,23,31,39,47,55,63};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    private boolean isLeftWall(int ind){
        int[] wall={0,8,16,24,32,40,48,56};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    public void play(int ind){
        if(!gameover){
            if(SetAble(ind)){
                //反悔
                gameLog.pushTableToList(table.clone());
                gameLog.pushSideToList(nowPresentB);


                if(nowPresentB) table[ind]='B';
                else table[ind]='W';
                Reverse(ind);
                Turn();
            }
        }
        SetableList();
        if(this.getSetAbleListSize()==0){

            Turn();
            SetableList();
            if(getSetAbleListSize()==0){
                gameover=true;
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
            SetableList();
        }
    }
    private boolean SetAble(int ind)
    {
        int t=setAble.size();
        for (int i=0;i<t;i++)
        {
            if(setAble.get(i)==ind){
                return true;
            }
        }
        return false;
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
    private void SetableList(){
        char me='W',opp='B';
        setAble.clear();
        if(nowPresentB){
            me='B';
            opp='W';
        }
        for (int i=0;i<cellnum;i++){
            if(table[i]==me){
                for (int j=0;j<total_direct_num;j++)
                {
                    if(!stepforward(i,j)){
                        continue;
                    }
                    int step=i+direct[j];
                    if(isInBoard(step))
                    {
                        if(table[step]==opp){//自己的下一個為對手色
                            while(stepforward(step,j))
                            {
                                step+=direct[j];
                                if(isInBoard(step)){
                                    if(table[step]==' ')
                                    {
                                        setAble.add(step);
                                        //setrevInd.add(step);
                                        //setrevDir.add(j);
                                        break;
                                    }
                                    else if(table[step]==me) break;//遇到自記就break
                                }
                                else break; //沒在棋盤內就break;
                            }
                        }
                    }
                }

            }
        }
    }
    private void Reverse(int ind)
    {
        char me='W',opp='B';
        if(nowPresentB){
            me='B';
            opp='W';
        }
        for (int i=0;i<total_direct_num;i++){
            if(stepforward(ind,i)){
                int step=ind+direct[i];
                if(table[step]==opp){
                    while(stepforward(step,i)){
                        step+=direct[i];
                        if(table[step]==me){ //翻轉
                            int tmp=ind+direct[i];
                            while(tmp!=step){
                                table[tmp]=me;
                                tmp+=direct[i];
                            }
                            break;
                        }
                        else if (table[step]==' ')
                        {
                            break;
                        }
                    }
                }
            }
        }

    }
    private boolean stepforward(int ind,int dir)
    {
        if(isUpWall(ind)&&(dir==0||dir==1||dir==7)){
            return false;
        }
        else if (isDownWall(ind)&&(dir==3||dir==4||dir==5)){
            return false;
        }
        else if (isLeftWall(ind)&&(dir==5||dir==6||dir==7)){
            return false;
        }
        else if (isRightWall(ind)&&(dir==1||dir==2||dir==3)){
            return false;
        }
        return true;
    }
    public int[] getSetAbleList(){
        int len=setAble.size();
        int[] list=new int[len];
        for (int i=0;i<len;i++){
            list[i]=setAble.get(i);
        }
        return list;
    }
    private int getSetAbleList(int ind){
        return setAble.get(ind);
    }
    private int getSetAbleListSize(){
        return setAble.size();
    }
    private void Turn(){
        nowPresentB=!nowPresentB;
    }
    public void setPresent(char prt,int ind){
        if(!isFill(ind)){
            table[ind]=prt;
            Turn();
        }
    }
    public char getPresent(int ind){
        return table[ind];
    }
    public boolean isPresentB(){
        return nowPresentB;
    }
    public char nowPresentCHAR(){
        if(nowPresentB) return 'B';
        else return 'W';
    }
    public boolean isFill(int ind){
        return table[ind] != ' ';
    }
    public char winner(){
        return win;
    }
}
