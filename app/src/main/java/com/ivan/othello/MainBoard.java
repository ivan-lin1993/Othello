package com.ivan.othello;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ivan on 2016/6/3.
 */
public class MainBoard extends View{
    private int h,w;
    private Bitmap bitmap;
    private Canvas buffer;
    private Paint paint;
    private Point[] hleftPoint,hrightPoint;
    private Point[] vupPoint,vdownPoint;

    public MainBoard(Context context) {
        super(context);
        paint=new Paint();
    }
    public MainBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
    }
    public MainBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        h=View.MeasureSpec.getSize(heightMeasureSpec);
        w=View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(w,h);
        bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        buffer=new Canvas(bitmap);

        calculateLinePlacements();
        drawBoard();
    }
    private void drawBoard(){
        //background
        paint.setColor(Color.rgb(0, 186, 120));;
        buffer.drawPaint(paint);

        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        for (int i=0;i<9;i++){
            buffer.drawLine(hleftPoint[i].x,hleftPoint[i].y,hrightPoint[i].x,hrightPoint[i].y,paint);
        }
        for (int i=0;i<9;i++){
            buffer.drawLine(vupPoint[i].x,vupPoint[i].y,vdownPoint[i].x,vdownPoint[i].y,paint);
        }

    }
    private void calculateLinePlacements(){
        int boardTop=h/8;
        int cellW=w/8;
        int cellH=cellW;
        //Create Point to draw board
        hleftPoint=new Point[9];
        hrightPoint=new Point[9];
        vupPoint=new Point[9];
        vdownPoint=new Point[9];

        for (int i=0;i<9;i++){
            Point p1=new Point(0,boardTop+i*cellH);
            hleftPoint[i]=p1;
        }
        for (int i=0;i<9;i++){
            Point p1=new Point(w,boardTop+i*cellH);
            hrightPoint[i]=p1;
        }
        for (int i=0;i<9;i++){
            Point p1=new Point(i*cellW,boardTop);
            vupPoint[i]=p1;
        }
        for (int i=0;i<9;i++){
            Point p1=new Point(i*cellW,boardTop+8*cellH);
            vdownPoint[i]=p1;
        }

    }
}
