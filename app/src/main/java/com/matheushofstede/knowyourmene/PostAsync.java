package com.matheushofstede.knowyourmene;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;



import android.os.AsyncTask;

/**
 * Created by he4dless on 25/02/15.
 */
public class PostAsync extends AsyncTask<Void, Void, Void> {

    XMLHelper helper;
    private Context myContext;
    String queryy;

    SQLHelper entry = new SQLHelper(myContext, queryy);
    MainActivity main;



    @Override
    protected void onPreExecute() {


        //pd = new ProgressDialog(context);

        //pd.setMessage("Baixando os mene tudo...");
        //pd.setCancelable(false);
        //pd.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //parsar o xml
        helper = new XMLHelper();
        helper.get();

        //preparar a database

        entry.open();

        entry.clearDatabase();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        for(PostValue post : helper.knowyourmene){


            String link = post.getLink();
            String tag1 = post.getTag1();
            String tag2 = post.getTag2();
            String tag3 = post.getTag3();



            entry.create(link, tag1, tag2, tag3);
            //colocar fora do for pra ver oq acontece
            main.populate();


        }
        //fecha database
        entry.close();
        //progress dialog fecha

    }



}