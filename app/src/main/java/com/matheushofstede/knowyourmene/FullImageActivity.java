package com.matheushofstede.knowyourmene;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FullImageActivity extends ActionBarActivity {
	ImageView imageview;
	public Bitmap bitmap;
	String fullimage;
    ProgressDialog pDialog;
    String fileName;
    FileOutputStream fos;
    FileOutputStream fo;
    File myDir;
    String dir;
    Bitmap myBitmap;


    String gText = "LOL";
    int scale = 10;
    boolean edited = false;
    
	
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		fullimage = (String) i.getExtras().get("id");
		fileName = fullimage.substring( fullimage.lastIndexOf('/')+1, fullimage.length() );
        Log.i("FILEMANE", fileName);
		
		imageview = (ImageView)findViewById(R.id.imageView1);






		
		new LoadImage().execute(fullimage);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fullimage, menu);
		
		return true;
	}
	
	@Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);


                return true;
            case R.id.share:
              new ShareImageTask().execute();
            return true;

           case R.id.save:
               new ImageDownloadAndSave().execute();
           return true;

            case R.id.edit:
               Toast.makeText(FullImageActivity.this, "Esta merda ainda esta por vir", Toast.LENGTH_LONG).show();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();






                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("image", byteArray);
                startActivity(i);
                // EditImage(gText);
                //imageview.setImageBitmap(bitmap);

            return true;

            default:
                return super.onOptionsItemSelected(item);  
        }  
	
	}
	
	
	
	
	
	
	
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
	    @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(FullImageActivity.this);
	            pDialog.setMessage("Carregando seu mene....");
	            pDialog.setCancelable(false);
	            pDialog.show();
	    }
	       protected Bitmap doInBackground(String... args) {
	         try {
	               bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

                 URL url = new URL(fullimage);
                 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                 connection.setDoInput(true);
                 connection.connect();
                 InputStream input = connection.getInputStream();
                 myBitmap = BitmapFactory.decodeStream(input);


	        } catch (Exception e) {
	              e.printStackTrace();
	        }
	      return bitmap;
	       }
	       protected void onPostExecute(Bitmap image) {
	         if(image != null){


	           imageview.setImageBitmap(image);

	           pDialog.dismiss();
	         }else{
	           pDialog.dismiss();
	           Toast.makeText(FullImageActivity.this, "Image não existe ou houve um erro na conexão", Toast.LENGTH_SHORT).show();
	         }
	       }
	   }
	
	
	
	
	
	public class ImageDownloadAndSave extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... arg0) 
        {           
            downloadImagesToSdCard("","");
            return null;
        }
        
        protected void onPostExecute(Bitmap image) {
	         if(fos != null){
	        	 Toast.makeText(FullImageActivity.this, "Ibagem salva na Galeria (vulgo /sdcard/Know Your Mene)", Toast.LENGTH_LONG).show();
	         }else{
	           pDialog.dismiss();
	           Toast.makeText(FullImageActivity.this, "Algo deu errado, cheque sua conexão", Toast.LENGTH_SHORT).show();
	         }
	       }

           private void downloadImagesToSdCard(String downloadUrl,String imageName)
            {
                Random t = new Random();
                int rand = t.nextInt(1000);
                int rand2 = t.nextInt(100);
                int rand3 = t.nextInt(1000);

                String sdCard=Environment.getExternalStorageDirectory().toString();
                   File knowyourmene =  new File(sdCard,"Know Your Mene");
                   myDir = new File(sdCard,"Know Your Mene/"+rand+rand2+rand3+fileName);

                   /*  if specified not exist create new */
                   if(!myDir.exists())
                   {
                       knowyourmene.mkdir();
                       myDir.mkdir();

                       Log.v("", "inside mkdir");
                   }else{


                   }

                   /* checks the file and if it already exist delete */
                   String fname = imageName;
                   File file = new File (myDir, fname);
                   if (file.exists ())
                       file.delete ();

                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (edited == true){
                       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                   }else{
                       myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                   }
                MediaScannerConnection.scanFile(getApplicationContext(), new String[] { myDir.getAbsolutePath() }, null, new OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) { }
                });



                   //*URL url = new URL(fullimage);
                   /* making a directory in sdcard


                        /* Open a connection
                   URLConnection ucon = url.openConnection();
                   InputStream inputStream = null;
                   HttpURLConnection httpConn = (HttpURLConnection)ucon;
                   httpConn.setRequestMethod("GET");
                   httpConn.connect();

                     if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) 
                     {
                      inputStream = httpConn.getInputStream();
                     }*/







                   /* int totalSize = httpConn.getContentLength();
                   int downloadedSize = 0;   
                   byte[] buffer = new byte[1024];
                   int bufferLength = 0;
                   while ( (bufferLength = inputStream.read(buffer)) >0 ) 
                   {                 
                     fos.write(buffer, 0, bufferLength);                  
                     downloadedSize += bufferLength;                 
                     Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
                   }   
                    	   MediaScannerConnection.scanFile(getApplicationContext(), new String[] { myDir.getAbsolutePath() }, null, new OnScanCompletedListener() {
                               @Override
                               public void onScanCompleted(String path, Uri uri) { }
                           });
                       Log.d("test", "Image Saved in sdcard..");                      
               }
               catch(IOException io)
               {                  
                    io.printStackTrace();
               }
               catch(Exception e)
               {                     
                   e.printStackTrace();
               }
               */
           }       

	
    }
	
	public class ShareImageTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FullImageActivity.this);
            pDialog.setMessage("Pera ainda....");
            pDialog.setCancelable(true);
            pDialog.show();
    }
		
		protected String doInBackground(String... arg0) {

                ShareImage();

                pDialog.dismiss();
                return null;

        }


		protected void onPostExecute(String result) {

            if(fo != null){
                Toast.makeText(FullImageActivity.this, "Pronto para compartilhar", Toast.LENGTH_LONG).show();
            }else{
                pDialog.dismiss();
                Toast.makeText(FullImageActivity.this, "Algo deu errado, cheque sua conexão", Toast.LENGTH_SHORT).show();
            }

	           //pDialog.dismiss();
	       }
		
		private void ShareImage() {
            try {



                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/png");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (edited == true){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                }else{
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                }

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temp_file.jpg");
                f.createNewFile();
                fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temp_file.jpg"));
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar usando:"));

                fo.close();
            }catch(Exception i){
                i.printStackTrace();

            }
			
		}	
	}



public showBottonSheet(){
	new BottomSheet.Builder(this)
            .title("Opções")
            .grid() // <-- important part
            .sheet(R.menu.bsfullimageactivity)
            .listener(new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO
        }
    }).show();
	
}
	
	
	}

	
	
		

	
	
	
