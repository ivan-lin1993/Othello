package com.ivan.othello;

import java.util.Stack;

/**
 * Created by Ivan Lin on 2016/9/26.
 */
public class GameLog {
    private Stack<char[]> tableLog=new Stack<>();
    private Stack<Boolean> isBlackSideLog=new Stack<>();
    public void pushTableToList(char[] table){
        tableLog.push(table);
    }
    public void pushSideToList(boolean side){
        isBlackSideLog.push(side);
    }
    public char[] popTableFromList(){
        return tableLog.pop();
    }
    public boolean popSideFromList(){
        return isBlackSideLog.pop();
    }
    public boolean isPopAble(){
        if(tableLog.size()>0){
            return true;
        }
        return false;
    }
}
