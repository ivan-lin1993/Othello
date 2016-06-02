package com.example.othello;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;


@SuppressLint({ "DrawAllocation", "NewApi" })
public class MainBoard extends View{
	private int h,w;
	private Bitmap bitmap;
	private Canvas buffer;
	private Paint paint;
	private Point[] hleftPoint,hrightPoint;
	private Point[] vupPoint,vdownPoint;
	
	private CellBoard cellBoard;
	private Game game=new Game();
//	private AI ai;
	private boolean nowAI=false;
	
	
	public MainBoard (Context context){
		super(context);
		paint= new Paint();
	}
	public void Restart(){
		//cellBoard.Restart();
		nowAI=false;
		game.GameInitial();
		buffer.drawColor(0,Mode.CLEAR);
		drawWB();
	}
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawBitmap(bitmap, 0, 0 ,paint);
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		h=View.MeasureSpec.getSize(heightMeasureSpec);
		w=View.MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(w,h);
		bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		buffer=new Canvas(bitmap);
		
		calculateLinePlacements();
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
//		buffer.drawLine(hLine01[0].x, hLine01[0].y, hLine01[1].x,hLine01[1].y, paint);
//		buffer.drawLine(hLine02[0].x, hLine02[0].y, hLine02[1].x,hLine02[1].y, paint);
//		buffer.drawLine(vLine01[0].x, vLine01[0].y, vLine01[1].x,vLine01[1].y, paint);
//		buffer.drawLine(vLine02[0].x, vLine02[0].y, vLine02[1].x,vLine02[1].y, paint);
		
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(100);
		paint.setTextSkewX((float) -0.5);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		buffer.drawText("Restart",w/2+80,h-80,paint);
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

		cellBoard=new CellBoard(cellW,cellH,boardTop,game);
	}
	public boolean TouchFunc (MotionEvent event,float a,float b){
		//float x=event.getX();
		//float y=event.getY();
		a=event.getX();
		b=event.getY();
		
		if(event.getAction()==MotionEvent.ACTION_UP){
			int a1=(int)a,b1=(int)b;
			if(a1>w/2+70&&a1<w/2+450&&b1>h-200&&b1<h-50){
				Restart();
				//if(nowAI)AIplay();
			}
			int setID=cellBoard.getCellInd(a, b);
			if(setID!=-1){
				buffer.drawColor(0,Mode.CLEAR);
				//char present=game.getPresent(cellBoard.getCellInd(a,b));
				game.play(setID);
				drawWB();
				nowAI=!nowAI;
			}
			if(game.isGameOver()){
				drawScore();
			}
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
	private void drawScore(){
		paint.setColor(Color.BLACK);
		paint.setTextSkewX(0);
		paint.setStrokeWidth(5);
		buffer.drawText("W:"+game.showScore('W')+"  B:"+game.showScore('B'),20,h-80,paint);
	}
	private void drawWB(){
		buffer.drawColor(0,Mode.CLEAR);
		drawBoard();
		for(int i=0;i<64;i++){
			char present=game.getPresent(i);
			RectF position=cellBoard.getRect(i);
			if(present=='W'){
				paint.setColor(Color.WHITE);
				paint.setStyle(Style.FILL);
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
			buffer.drawOval(rect.left+offset, rect.top+offset, rect.right-offset, rect.bottom-offset, paint);
		}

	}

}


