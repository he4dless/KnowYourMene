package com.matheushofstede.knowyourmene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by he4dless on 04/03/15.
 */
public class EditActivity extends ActionBarActivity {
    Bitmap bitmap;
    Bitmap newBitmap;
    ImageView imageview;
    String gText = "LOL";
    String tcima = "Loren ipsum dolor sit";
    String tbaixo = "Loren Ipsum";
    int scale = 10;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageview = (ImageView)findViewById(R.id.imageView2);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //Intent i = getIntent();
        //bitmap = (Bitmap) getIntent().getParcelableExtra("image");



        EditImage(tcima, tbaixo);
        imageview.setImageBitmap(bitmap);
        //imageview.setImageBitmap(EditImage(gText));



    }




    public Bitmap EditImage(String tcima, String tbaixo){
        Typeface tf = Typeface.createFromAsset(getAssets(), "impact-opt.ttf");

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setARGB(255, 0, 0, 0);

        strokePaint.setTextSize((int) (8 * scale));
        strokePaint.setTypeface(tf);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);

        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(tf);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(225, 225, 225));
        // text size in pixels
        paint.setTextSize((int) (tbaixo.length() / bitmap.getWidth()));  //era 10 SCALE 8 * scale


        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.measureText(tbaixo, 0, tbaixo.length());
        bounds.left += (bitmap.getWidth() - bounds.right) / 2.0f;
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int cimax = (int) ((bitmap.getWidth() / 2) - (tbaixo.length()*15));
        int cimay = (bitmap.getHeight()/2)-(bitmap.getHeight()/4);
        int baixox = (int) ((bitmap.getWidth() / 2) - (tbaixo.length()*15));
        //int baixox = (bitmap.getWidth() - bounds.width())/2;
        int baixoy = ((bitmap.getHeight()/2)+(bitmap.getHeight())/2-(bitmap.getHeight())/10);
        //-(bitmap.getHeight())/10

        //int y = (bitmap.getHeight()/2 + (bitmap.getHeight()/4)*2);
        //int x = (bitmap2.getWidth() - bounds.width())/2;
        //int y = ((bitmap2.getHeight()/2)+(bitmap.getHeight())/2-(bitmap.getHeight())/10);

        //cima
        strokePaint.getTextBounds(tcima, 0, tcima.length(), bounds);
        //cima
        canvas.drawText(tcima, cimax, cimay, paint);
        canvas.drawText(tcima, cimax, cimay, strokePaint);



        //baixo
        strokePaint.getTextBounds(tbaixo, 0, tbaixo.length(), bounds);


        //baixo
        canvas.drawText(tbaixo, baixox, baixoy, paint);
        canvas.drawText(tbaixo, baixox, baixoy, strokePaint);


        return bitmap;



    }


    public Bitmap drawTextToBitmap(String gText) {

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (140 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);
        Log.i("TGA","RETURNBITMAP");

        return bitmap;

    }



    private Bitmap ProcessingBitmap(){
        Bitmap bm1 = null;
        Bitmap newBitmap = null;


        Bitmap.Config config = bitmap.getConfig();
        if(config == null){
            config = Bitmap.Config.ARGB_8888;
        }

        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas newCanvas = new Canvas(newBitmap);

        newCanvas.drawBitmap(newBitmap, 0, 0, null);


        if(gText != null){

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.BLUE);
            paintText.setTextSize(50);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();
            paintText.getTextBounds(gText, 0, gText.length(), rectText);

            newCanvas.drawText(gText,
                    0, rectText.height(), paintText);

            Toast.makeText(getApplicationContext(),
                    "drawText: " + gText,
                    Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(),
                    "caption empty!",
                    Toast.LENGTH_LONG).show();
        }

        return newBitmap;
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editimage, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();


                return true;

            case R.id.mais:
               //aumentar letra
                return true;

            case R.id.menos:
               //diminuir letra
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
