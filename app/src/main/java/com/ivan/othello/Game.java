package com.ivan.othello;

import java.util.ArrayList;
import java.util.List;

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


    private List<Integer> setAble = new ArrayList<Integer>();
    private List<Integer> setrevInd = new ArrayList<Integer>();
    private List<Integer> setrevDir =new ArrayList<Integer>();
    Game(){
        GameInitial();
    }

    public void GameInitial(){
        win=' ';
        nowPresentB=true;
        for (int i=0;i<cellnum;i++){
            table[i]=' ';
        }
        gameover=false;
//		table[59]='B';
//		table[51]='W';
//		table[43]='W';
//		table[61]='B';
//		table[53]='W';
        //table[27]='B';
        table[27]='W';
        table[36]='W';
        table[28]='B';
        table[35]='B';
        SetableList();
    }

    public boolean isGameOver(){
        return gameover;

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
        int length=wall.length;
        for (int i=0;i<length;i++){
            if(ind==wall[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isUpWall(int ind){
        int[] wall={0,1,2,3,4,5,6,7};
        int length=wall.length;
        for (int i=0;i<length;i++){
            if(ind==wall[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isDownWall(int ind){
        int[] wall={56,57,58,59,60,61,62,63};
        int length=wall.length;
        for (int i=0;i<length;i++){
            if(ind==wall[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isRightWall(int ind){
        int[] wall={7,15,23,31,39,47,55,63};
        int length=wall.length;
        for (int i=0;i<length;i++){
            if(ind==wall[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isLeftWall(int ind){
        int[] wall={0,8,16,24,32,40,48,56};
        int length=wall.length;
        for (int i=0;i<length;i++){
            if(ind==wall[i]){
                return true;
            }
        }
        return false;
    }
    public void play(int ind){
        if(!gameover){
            if(SetAble(ind)){
                if(nowPresentB) table[ind]='B';
                else table[ind]='W';
                Reverse(ind);
                Turn();
            }
        }
        SetableList();
        if(this.getSetAbleListSize()==0){
            showToast();
            Turn();
            SetableList();
            if(getSetAbleListSize()==0){
                gameover=true;
            }
        }
    }
    private void showToast(){

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
    public String showScore(char color){
        Integer count=0;
        for (int i=0;i<64;i++){
            if(table[i]==color){
                count++;
            }
        }

        return count.toString();
    }
    private void SetableList(){
        char me='W',opp='B';
        setAble.clear();
        setrevInd.clear();
        setrevDir.clear();
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
                                        setrevInd.add(step);
                                        setrevDir.add(j);
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
    public int getSetAbleList(int ind){
        return setAble.get(ind);
    }
    public int getSetAbleListSize(){
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
        if(table[ind]!=' ') return true;
        else return false;
    }
    public char winner(){
        return win;
    }
}
