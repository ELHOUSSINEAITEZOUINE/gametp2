package ma.fpt.game2.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Control {
    public Bitmap bitmap;

    Rect rect;
    public int x=0,y=0;

    public Control(Resources res, int id) {
        bitmap= BitmapFactory.decodeResource(res,id);
        bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/4,bitmap.getHeight()/4,false);
    }

    public Rect getRect() {
        rect=new Rect(x,y, x+bitmap.getWidth(),y+bitmap.getHeight());
        return rect;
    }
}
