package ma.fpt.game2.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import ma.fpt.game2.R;


public class Mario {

    public int x,y,width,height;

    //Déclaration des états possible de Bitmap Mario
    public Bitmap mario_stop, mario_marche,mario_jump;

    //référence vers l'état courant
    public Bitmap currentState;
    public boolean isFlipedToRight=false;
    public int stateCounter=0;

    Mario(Resources res){
        mario_stop= BitmapFactory.decodeResource(res, R.drawable.mario_stop);
        mario_marche= BitmapFactory.decodeResource(res, R.drawable.mario_marche);
        mario_jump= BitmapFactory.decodeResource(res, R.drawable.mario_jump);


        x=100;
        y=0;


        mario_stop=Bitmap.createScaledBitmap(mario_stop,mario_stop.getWidth()/6,mario_stop.getHeight()/6,false);
        mario_marche=Bitmap.createScaledBitmap(mario_marche,mario_marche.getWidth()/6,mario_marche.getHeight()/6,false);
        mario_jump=Bitmap.createScaledBitmap(mario_jump,mario_jump.getWidth()/6,mario_jump.getHeight()/6,false);

        //par défaut, Mario est orienté à gauche, alors on change vers le droit
        flipMario();
        isFlipedToRight=true;

        currentState=mario_stop;

        width=currentState.getWidth();
        height=currentState.getHeight();


    }

    //founction qui permet de changer l'orientation de tous les états
    public void  flipMario(){
        mario_stop=Bitmap.createBitmap(flipHorizontal(mario_stop));
        mario_marche=Bitmap.createBitmap(flipHorizontal(mario_marche));
        mario_jump=Bitmap.createBitmap(flipHorizontal(mario_jump));

    }

    //fonction qui permet de changer l'orientation du Bitmap
    public Bitmap flipHorizontal(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //retourne le rectangle correspondat au bitmap courant
    public Rect getRect() {
        Rect rect=new Rect(x,y, x+currentState.getWidth(),y+currentState.getHeight());
        return rect;
    }


    //fonction appelée lors de mouvement horizontal du Mario
    public void changeStatStopAndMarch(){
        if(stateCounter==0){
            stateCounter++;
            currentState=mario_stop;
        }
        else {
            stateCounter--;
            currentState=mario_marche;
        }

    }


}
