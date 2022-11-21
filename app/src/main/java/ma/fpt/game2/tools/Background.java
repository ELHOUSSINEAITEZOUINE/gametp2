package ma.fpt.game2.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ma.fpt.game2.R;

public class Background {
    int x=0, y=0;
    Bitmap background;

    public Background(int screenX, int screenY, Resources res) {
        background= BitmapFactory.decodeResource(res, R.drawable.background);
        background=Bitmap.createScaledBitmap(background,screenX,screenY,false);
    }
}
