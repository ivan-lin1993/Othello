package com.ivan.othello;

import com.ivan.othello.ai.AI;

/**
 * Created by Ivan Lin on 2016/10/13.
 */

public class Heuristic {
    static int val[]={
            20,-3,11, 8, 8,11,-3,20,
            -3,-7,-4, 1, 1,-4,-7,-3,
            11,-4, 2, 2, 2, 2,-4,11,
             8, 1, 2,-3,-3, 2, 1, 8,
             8, 1, 2,-3,-3, 2, 1, 8,
            11,-4, 2, 2, 2, 2,-4,11,
            -3,-7,-4, 1, 1,-4,-7,-3,
            20,-3,11, 8, 8,11,-3,20};
    public static int totalScore(char []table,char Me,int Stage){
        int score=0;
        char me='B';
        char opp='W';
        if(Me!=me){
            me='W';
            opp='B';
        }
        if(Stage== AI.STAGE1){
            score=GameRule.getSetableList(table,Me).size();
        }
        else if(Stage==AI.STAGE2){
            for(int i=0;i<64;i++){
                if(table[i]==me){
                    score+=val[i];
                }
                else if(table[i]==opp) score-=val[i];
            }
        }
        else if(Stage==AI.STAGE3){
            for(int i=0;i<64;i++){
                if(table[i]==me){
                    score++;
                }
            }
        }
        return score;
    }
    public static float CoinParty(int Max,int min){
        return 100*((float)(Max-min)/(float)(Max+min));
    }
    public static float Mobility(int Max,int min){
        if(Max+min!=0){
            return 100*((float)(Max-min)/(float)(Max+min));
        }
        else{
            return 0.0f;
        }
    }
}
