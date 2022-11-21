package ma.fpt.game2.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;

import ma.fpt.game2.R;

public class GameView extends SurfaceView implements Runnable {

   private Thread thread;
   private boolean isPlay;
   private int screenX, screenY;

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

   public GameView(Context context, int screenX, int screenY) {
      super(context);

      this.screenX = screenX;
      this.screenY = screenY;

      Bitmap tileset = BitmapFactory.decodeResource(getResources(), R.drawable.tileset);
      tileH = tileset.getHeight()/2;
      tileW = 98/2;

      Log.e("TILESET WIDTH", tileset.getWidth()+"");
      Log.e("TILESET Height * 10", (tileH)+"");
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

      tuiles=new Bitmap[11];
      for(int i=0;i<11;i++){
         Bitmap tile = Bitmap.createBitmap(tileset, (tileW*2) * i, 0, (tileW*2), (tileH*2));
         tile = Bitmap.createScaledBitmap(tile, tileW, tileH, false);
         tuiles[i] = tile;
      }



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

         }
         getHolder().unlockCanvasAndPost(canvas);
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

}
