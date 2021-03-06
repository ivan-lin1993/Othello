package com.ivan.othello.ai;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.ivan.othello.Game;

/**
 * Created by Ivan Lin on 2016/9/27.
 */
public class AI extends AsyncTask<Game,Void,Integer>{
    public AsyncResponse delegate = null;
    private Context thisContext;
    protected Integer result=0;
    private ProgressDialog pDialog;
    private String loadstr="Loading...";
    static public int STAGE1=1,STAGE2=2,STAGE3=3;

    public AI(Context context,AsyncResponse delegate){
        thisContext=context;
        this.delegate=delegate;
    }
    public interface AsyncResponse {
        void processFinish(Integer output);
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        pDialog=new ProgressDialog(thisContext);
        pDialog.setMessage(loadstr);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    @Override
    protected void onPostExecute(Integer _result) {
        super.onPostExecute(_result);
        pDialog.dismiss();
    }
    @Override
    protected Integer doInBackground(Game... params) {
        return null;
    }
    public static int gameStage(char []table){
        int count=0;
        for (char c:table) {
            if(c!=' '){
                count++;
            }
        }
        if(count>54) return STAGE3;
        else if (count>20) return STAGE2;
        else return STAGE1;
    }
}
