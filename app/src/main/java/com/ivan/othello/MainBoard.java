package com.ivan.othello;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
    private CellBoard cellBoard;
    private Game game;
    public MainBoard(Context context) {
        super(context);
        init();
    }
    public MainBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MainBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init()
    {
        paint=new Paint();
        game=new Game();
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
        drawWB();
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
        cellBoard=new CellBoard(cellW,cellH,boardTop);
    }
    private void drawWB(){
        buffer.drawColor(0, PorterDuff.Mode.CLEAR);
        drawBoard();
        for(int i=0;i<64;i++){
            char present=game.getPresent(i);
            RectF position=cellBoard.getRect(i);
            if(present=='W'){
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                buffer.drawOval(position, paint);
            }
            else if (present=='B'){
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                buffer.drawOval(position, paint);
            }
        }

        drawCanSet();

        invalidate();
    }
    private void drawCanSet(){
        float offset=45;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        if(game.nowPresentCHAR()=='W'){
            paint.setColor(Color.WHITE);
        }
        else paint.setColor(Color.BLACK);
        int size=game.getSetAbleListSize();
        for (int i=0;i<size;i++){
            RectF rect = cellBoard.getRect(game.getSetAbleList(i));
            buffer.drawOval(rect.left+offset, rect.top + offset, rect.right-offset, rect.bottom-offset, paint);
        }

    }
    public boolean TouchFunc (MotionEvent event,float a,float b){
        //float x=event.getX();
        //float y=event.getY();
        a=event.getX();
        b=event.getY();

        if(event.getAction()== MotionEvent.ACTION_UP){
            int a1=(int)a,b1=(int)b;
            int setID=cellBoard.getCellInd(a, b);
            if(setID!=-1){
                buffer.drawColor(0, PorterDuff.Mode.CLEAR);
                //char present=game.getPresent(cellBoard.getCellInd(a,b));
                game.play(setID);
                drawWB();
                //nowAI=!nowAI;
            }
            /*
            if(game.isGameOver()){
                drawScore();
            }
            */
//			if(game.isGameOver()){
//				Restart();
//			}
			/*else if(!game.isGameOver()){

				if(!nowAI){
					RectF position=cellBoard.getCellToFill(a, b);
					if(position!=null){
						char present=game.getPresent(cellBoard.getCellInd(a,b));
						drawXO(position,present);
						nowAI=!nowAI;
					}
				}
				//AI turn............
				if(game.isGameOver()){
					drawLine();
				}
				else if(nowAI) {
					AIplay();

				}
				if(game.isGameOver()){
					drawLine();
				}
			}
			*/
            invalidate();
        }
        return true;
    }
    class CellBoard{
        private int w;
        private int h;
        private int offset;
        protected Cell[] position;
        private int cell_num=64;

        public CellBoard(int cellWidth,int cellHeight,int t_offset){
            w=cellWidth;
            h=cellHeight;
            offset=t_offset;

            initialBoardCells();
        }
        private class Cell extends RectF {
            private int ind;
            public Cell(float left, float top,float right,float bottom,int i){
                super(left,top,right,bottom);
                ind=i;
            }
        }
        private void initialBoardCells(){
            int space=5;
            int done=0;
            position=new Cell[cell_num];
            int id=0;
            for(int i=0;i<8;i++){
                for(int j=0;j<8;j++){
                    position[id]= new Cell(j*w+space, (i)*h +space+ offset,(j+1)*w-space,(i+1)*h+offset-space,id);
                    id++;
                }

            }
            done=1;
        }
        public int getCellInd(float x,float y){
            for (Cell bp: position){
                if(bp.contains(x, y)){
                    return bp.ind;
                }
            }
            return -1;
        }
        public RectF getCellToFill(float x,float y){
            for (Cell bp: position){

                if(bp.contains(x, y)){//&&!game.isFill(bp.ind)){
                    RectF retCell=new RectF(bp);
                    game.play(bp.ind);
                    return retCell;
                }
            }
            return null;
        }
        public float getPositionX(int ind){
            return position[ind].centerX();
        }
        public float getPositionY(int ind){
            return position[ind].centerY();
        }
        public RectF getRect(int ind){
            RectF retCell=new RectF(position[ind].left,position[ind].top,position[ind].right,position[ind].bottom);
            return retCell;
        }
    }
}

