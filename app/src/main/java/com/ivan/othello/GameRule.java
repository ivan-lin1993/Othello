package com.ivan.othello;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan Lin on 2016/9/27.
 */
public class GameRule {
    static private int BOARD_SPACE_NUM=64;
    static private int[] direct={-8,-7,1,9,8,7,-1,-9};
    static public char[] Reverse(char []table,int put,char color){
        char me='W',opp='B';
        if(color=='B'){
            me='B';
            opp='W';
        }
        for (int i=0;i<direct.length;i++){
            if(stepforward(put,i)){
                int step=put+direct[i];
                if(table[step]==opp){
                    while(stepforward(step,i)){
                        step+=direct[i];
                        if(table[step]==me){ //翻轉
                            int tmp=put+direct[i];
                            while(tmp!=step){
                                table[tmp]=me;
                                tmp+=direct[i];
                            }
                            break;
                        }
                        else if (table[step]==' ') break;
                    }
                }
            }
        }
        return table;
    }
    static public boolean isGameOver(char []table,char color){
        int count=0;
        char op='B';
        if (op==color) op='W';
        for(int i=0;i<BOARD_SPACE_NUM;i++){
            if (table[i]!=' ') count+=1;
        }
        if(count==64) return true;
        else{
            if(getSetableList(table,color).size()==0&&getSetableList(table,op).size()==0) {
                return true;
            }
        }
        return false;
    }
    static public int caculateScore(char []table,char color){
        int count=0;
        for (int i=0;i<64;i++){
            if(table[i]==color){
                count++;
            }
        }
        return count;
    }
    static private boolean stepforward(int ind,int dir)
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
    static private boolean isUpWall(int ind){
        int[] wall={0,1,2,3,4,5,6,7};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    static private boolean isDownWall(int ind){
        int[] wall={56,57,58,59,60,61,62,63};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    static private boolean isRightWall(int ind){
        int[] wall={7,15,23,31,39,47,55,63};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    static private boolean isLeftWall(int ind){
        int[] wall={0,8,16,24,32,40,48,56};
        for (int aWall : wall) {
            if (ind == aWall) {
                return true;
            }
        }
        return false;
    }
    static public List<Integer> getSetableList(char[] table, char color){
        char me='W',opp='B';
        List<Integer> setAble=new ArrayList<>();
        if(color=='B'){
            me='B';
            opp='W';
        }
        for (int i=0;i<BOARD_SPACE_NUM;i++){
            if(table[i]==me){
                for (int j=0;j<direct.length;j++)
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
        return setAble;
    }
    static private boolean isInBoard(int ind){
        return ind<64&&ind>=0;
    }
}
