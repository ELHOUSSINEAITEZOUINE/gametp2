package ma.fpt.game2.tools;

import android.graphics.Rect;

public class Obstacle  {

   Rect rect;

   public Boolean isToWin=false;

   public int top=0,left=0, right=0,bottom=0;

   public int i=0,j=0;

   public Obstacle() {

   }

   public Rect getRect() {
      rect=new Rect(left,top,right,bottom);
      return rect;
   }

}