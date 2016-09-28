package com.ivan.othello.test;

/**
 * Created by Ivan Lin on 2016/9/28.
 */
public class Table {
    public void table1(char []table){
        init(table);
        setWhiteTable(table,14,19,20,21,22,23,26,27,28,29,30,35,36,38,43,45,50,51,52,53);
        setBlackTable(table,3,4,5,6,12,37,39,44,46,55,58,59,60,61,62,63);
    }
    public void talbe2(char []table){
        init(table);
        setBlackTable(table,9,27,52,54);
        setWhiteTable(table,0,1,8,53);
    }
    private void init(char []table){
        for(int i=0;i<64;i++){
            table[i]=' ';
        }
    }
    private void setBlackTable(char []table,int...id){
        for (int i:id){
            table[i]='B';
        }
    }
    private void setWhiteTable(char []table,int...id){
        for (int i:id){
            table[i]='W';
        }
    }
}
