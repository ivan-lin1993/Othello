package com.example.othello;

import android.graphics.RectF;

public class CellBoard {
	private int w;
	private int h;
	private int offset;
	protected Cell[] position;
	private int cell_num=64;
	private Game game=new Game();
	
	public CellBoard(int cellWidth,int cellHeight,int t_offset,Game g){
		w=cellWidth;
		h=cellHeight;
		offset=t_offset;
		initialBoardCells();
		game=g;
	}
	private class Cell extends RectF{
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

