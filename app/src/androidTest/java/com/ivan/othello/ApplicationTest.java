package com.ivan.othello;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.junit.Assert;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    Game game;
    char []temp_table=new char[64];
    public ApplicationTest() {
        super(Application.class);
        game=new Game(getContext());
        game.GameInitial();
        initTalbe();
    }
    private void initTalbe(){
        for (int i=0;i<64;i++){
            temp_table[i]=' ';
        }
    }
    private void setBlackTable(int...id){
        for (int i:id){
            temp_table[i]='B';
        }
    }
    private void setWhiteTable(int...id){
        for (int i:id){
            temp_table[i]='W';
        }
    }
    public void testAPI1() throws Exception {
        Assert.assertTrue(true);
    }
    public void testAPI2() throws Exception {
        setBlackTable(28,35);
        setWhiteTable(27,36);
        Assert.assertArrayEquals(game.getTable(),temp_table);
    }

    public void testAPI3() throws Exception {
        //initTalbe();
        game.play(19);
        game.play(18);
        setBlackTable(19,28,35);
        setWhiteTable(18,27,36);
        Assert.assertArrayEquals(game.getTable(),temp_table);
    }
    public void testAPI4() throws Exception{
        game.play(19);
        game.play(18);
        setBlackTable(19,28,35);
        setWhiteTable(18,27,36);
        Log.e("gamerule",String.valueOf(GameRule.getSetableList(game.getTable(),'B')));
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}