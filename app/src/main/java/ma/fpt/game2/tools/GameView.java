package ma.fpt.game2.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

import ma.fpt.game2.R;

public class GameView extends SurfaceView implements Runnable {

   private Thread thread;
   private boolean isPlay;
   private int screenX, screenY;

   boolean gameOver = false;

   Bitmap[] tuiles;
   int[][] tilMap;

   int step=30;

   private int score=0;

   int tileH, tileW;

   Bitmap coverBGBitmap;

   int coverBitmapX=0, coverBitmapY=0;

   int refX = 0, refY= 0;

   Canvas canvas;

   ArrayList<Obstacle> obstacles=new ArrayList<>();

   private Paint paint,colispaint;

   Control rightControl,leftControl,jumbControl;

   Mario mario;

   int cnt = 0;

   private boolean isMovingRight=false;
   private boolean isMovingLeft=false;

   public GameView(Context context, int screenX, int screenY) {
      super(context);

      this.screenX = screenX;
      this.screenY = screenY;

      Bitmap tileset = BitmapFactory.decodeResource(getResources(), R.drawable.tileset);
      tileH = tileset.getHeight()/2;
      tileW = (tileset.getHeight()-3)/2;

      Log.e("TILESET WIDTH", tileset.getWidth()+"");
      Log.e("TILESET Height", (tileH)+"");
      Log.e("TILESET WIDTH * 10", (tileH*10)+"");

      coverBGBitmap = Bitmap.createBitmap(tileW * 32, tileH * 13, Bitmap.Config.ARGB_8888);
      canvas = new Canvas(coverBGBitmap);

      paint=new Paint();

      tilMap = new int[][]{
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 3, 2, 3, 0, 0, 4, 5, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
              {1, 1, 1, 1, 1, 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
      };

/*      int[][] tileMapT = new int[0][0];

      for(int i=0; i<32; i++){
         for(int j=0; j<13; j++){
            tileMapT[i][j] = tilMap[j][i];
         }
      }*/

      //tilMap = tileMapT;

      tuiles=new Bitmap[11];
      for(int i=0;i<11;i++){
         Bitmap tile = Bitmap.createBitmap(tileset, (tileW *2)* i, 0, (tileW*2), (tileH*2));
         tile = Bitmap.createScaledBitmap(tile, tileW, tileH, false);
         tuiles[i] = tile;
      }


      rightControl=new Control(getResources(),R.drawable.rightcontrol);
      leftControl=new Control(getResources(),R.drawable.leftcontrol);
      jumbControl=new Control(getResources(),R.drawable.jumpcontrol);

      mario=new Mario(getResources());

      makeBaground();

      AddBtnControlle();

   }

   public void AddBtnControlle() {

   }

   public void makeBaground() {
      for (int i = 0; i < 13; i++) {
         for (int j = 0; j < 32; j++) {
            canvas.drawBitmap(tuiles[tilMap[i][j]], tileW * j, tileH * i, paint);
            Obstacle ob=new Obstacle();
            if(!(tilMap[i][j]==0 || tilMap[i][j]==1)){
               ob.top=i*tileH;
               ob.left=j*tileW;
               ob.bottom=ob.top+tileH;
               ob.right=ob.left+tileW;

               if(tilMap[i][j]==2){
                  ob.isToWin=true;
               }

               ob.i=i;
               ob.j=j;
               obstacles.add(ob);
            }


         }
      }
   }

   @Override
   public void run() {
      while (isPlay) {
         update();
         draw();
         sleep();
      }
   }

   public void update() {
      cnt+=2;

      if(isMovingRight){
         if((coverBitmapX+coverBGBitmap.getWidth()>screenX+30)){
            refX--;
         }else{
            if(mario.x<screenX/2)
               mario.x=mario.x+step;
         }
         mario.changeStatStopAndMarch();
      }


      if(isMovingLeft){
         if((coverBitmapX<0)){
            refX++;
         }else{
            if(mario.x>100)
               mario.x=mario.x-step;
         }
         mario.changeStatStopAndMarch();
      }

   }


   public void draw() {
      if (getHolder().getSurface().isValid()) {
         Canvas canvas = getHolder().lockCanvas();

         refY=-(coverBGBitmap.getHeight()-screenY);

         coverBitmapX=step*refX;
         coverBitmapY=refY;
         canvas.drawBitmap(coverBGBitmap, coverBitmapX, coverBitmapY,null);

         for(int i=0;i<obstacles.size();i++){
            Obstacle b= obstacles.get(i);
            Obstacle ob=new Obstacle();

            ob.top=b.top+refY;
            ob.bottom=b.bottom+refY;
            ob.right=b.right+coverBitmapX;
            ob.left=b.left+coverBitmapX;


            //canvas.drawRect(ob.getRect(),paint);

            if(ob.getRect().intersect(mario.getRect()) && b.isToWin==false){
               //canvas.drawText("Collision",(screenX/2)-80,150,colispaint);
               gameOver = true;

            }

            if(ob.getRect().intersect(mario.getRect()) && b.isToWin==true){
               score+=5;

               obstacles.remove(i);

               tilMap[b.i][b.j]=0;

               makeBaground();

            }

         }

         Rect floor=new Rect();
         floor.set(coverBitmapX,coverBitmapY+(11*tileH),coverBitmapX+coverBGBitmap.getWidth(),coverBitmapY+coverBGBitmap.getHeight());


         if(!mario.getRect().intersect(floor)){
            mario.y+=cnt;
         }

         canvas.drawBitmap(mario.currentState,mario.x,mario.y,paint);

         //canvas.drawRect(mario.getRect(),paint);

         int marge=40;

         rightControl.x=screenX - (rightControl.bitmap.getWidth()+marge);
         rightControl.y=screenY - (rightControl.bitmap.getHeight()+marge);

         leftControl.x=screenX - (leftControl.bitmap.getWidth()*2+marge);
         leftControl.y=screenY - (leftControl.bitmap.getHeight()+marge);

         jumbControl.x= marge;
         jumbControl.y=screenY - (jumbControl.bitmap.getHeight()+marge);

         canvas.drawBitmap(rightControl.bitmap, rightControl.x,rightControl.y,null);
         canvas.drawBitmap(leftControl.bitmap, leftControl.x,leftControl.y,null);
         canvas.drawBitmap(jumbControl.bitmap, jumbControl.x,jumbControl.y,null);


         getHolder().unlockCanvasAndPost(canvas);
         if(gameOver){
            pause();
         }
      }
   }

   public void sleep() {
      try {
         Thread.sleep(80);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public void resume() {
      thread = new Thread(this);
      thread.start(); // call run method of runnable
      isPlay = true;
   }

   public void pause() {
      try {
         thread.join(); // stop the thread
         isPlay = false;
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   private void init() {
      coverBitmapX=0;
      coverBitmapY=0;

      refX = 0;
      refY= 0;
      tilMap = new int[][]{
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 10, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 3, 2, 3, 0, 0, 4, 5, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 7, 0, 0, 0},
              {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
              {1, 1, 1, 1, 1, 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
      };

      mario=new Mario(getResources());

      makeBaground();

      paint = new Paint();
      resume();
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {

      switch (event.getAction()) {
         case MotionEvent.ACTION_DOWN:

            if(gameOver){
               init();
            }else{
               int xevent= Math.round(event.getX());
               int yevent= Math.round(event.getY());

               //si, on clique sur le bouton droit
               if (rightControl.getRect().contains(xevent,yevent) ) {

                  //on change l'??tat de Mario ?? ??tat de marche
                  mario.currentState=mario.mario_marche;
                  //on pivote verticalement le bitap du Mario vers le droit, si ce n'est pas le cas
                  if(!mario.isFlipedToRight){
                     mario.flipMario();
                     mario.isFlipedToRight=true;
                  }

                  int[][] newtilMap = tilMap;

                  for (int i=0; i<13; i++){
                     for (int j=0; j< 32; j++){
                        if(j == 31){
                           newtilMap[i][j] = tilMap[i][0];
                        }else{
                           newtilMap[i][j] = tilMap[i][j];
                        }

                     }
                  }

                  tilMap = newtilMap;


                  if(mario.x < screenX/2){
                     isMovingRight=true;
                  }

                  makeBaground();

                  //isMovingRight=true;
               }

               //si, on clique sur le gauche droit
               if ( leftControl.getRect().contains(xevent,yevent) ) {
                  //on change l'??tat de Mario ?? ??tat de marche
                  mario.currentState=mario.mario_marche;
                  //on pivote verticalement le bitmap du Mario vers la gauche, si ce n'est pas le cas
                  if(mario.isFlipedToRight){
                     mario.flipMario();
                     mario.isFlipedToRight=false;
                  }

                  int[][] newtilMap = tilMap;

                  for (int i=0; i<13; i++){
                     for (int j=0; j< 32; j++){
                        if(i == 31){
                           newtilMap[i][j] = tilMap[0][j];
                        }else{
                           newtilMap[i][j] = tilMap[i][j];
                        }

                     }
                  }

                  tilMap = newtilMap;

                  if(mario.x+50 > screenX/2){
                     isMovingLeft=true;
                  }
               }

               //si, on clique sur le bouton saut
               if(jumbControl.getRect().contains(xevent,yevent)){
                  //on change l'??tat de Mario ?? ??tat du saut

                  //on fait remonter Mario vers le haut
                  if(mario.y > (screenY/2)-100){
                     mario.currentState=mario.mario_jump;
                     mario.y-= (screenY/2)-100; // 700 - 200  = 500
                     cnt=0;
                  }
                  //initiliser le conteur d'animation de Mario


               }

            }

            break;

         case MotionEvent.ACTION_UP:

            if(gameOver){
               init();
            }else {
               mario.currentState=mario.mario_stop;
               isMovingRight=false;
               isMovingLeft=false;
            }

            break;
      }

      return  true;
   }


}
