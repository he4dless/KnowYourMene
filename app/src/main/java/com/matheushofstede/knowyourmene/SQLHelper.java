package com.matheushofstede.knowyourmene;

import java.io.File;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;

import com.nostra13.universalimageloader.core.ImageLoader;

public class SQLHelper {
	
	public static final String DATABASE_NAME = "he4dless";
	
	public static final String DATABASE_TABLE = "menes";
	public static final int DATABASE_VERSION = 1;
	
	public static final String KEY_ID = "_id";
	public static final String KEY_LINK = "link";
	
	public static final String TAG_1 = "tag1";
	public static final String TAG_2 = "tag2";
	public static final String TAG_3 = "tag3";
	
	private DbHelper myHelper;
	private Context myContext;
	private SQLiteDatabase myDatabase;
	
	public static final String[] ALL_KEYS = new String[] {KEY_ID, KEY_LINK, TAG_1, TAG_2, TAG_3};

    private String queryy;

    public SQLHelper(Context c,String queryy){
        myContext = c;
        this.queryy = queryy;

    }

	
	
	private static class DbHelper extends SQLiteOpenHelper {


	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				KEY_LINK + " TEXT NOT NULL, "+
				TAG_1 + " TEXT NOT NULL, " + 
				TAG_2 + " TEXT NOT NULL, " + 
				TAG_3 + " TEXT NOT NULL);"
				);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		onCreate(db);
		
	}
	
	
	
	

}

	public SQLHelper open(){
		myHelper = new DbHelper(myContext);
		myDatabase = myHelper.getWritableDatabase();
		
		
		
		
		
		
		
		
		return this;
	}
	public void close(){
		myHelper.close();
	}
	public long create(String link, String tag1, String tag2, String tag3) {
		ContentValues cv = new ContentValues();
		
		
		cv.put(KEY_LINK, link);
		cv.put(TAG_1, tag1);
		cv.put(TAG_2, tag2);
		cv.put(TAG_3, tag3);
		
		
		return myDatabase.insert(DATABASE_TABLE, null, cv);
		
		
		
		
	}
	
	public void clearDatabase() {
		   myHelper = new DbHelper(myContext);
		   myDatabase = myHelper.getWritableDatabase();
		   myDatabase.delete(DATABASE_TABLE, null, null); //erases everything in the table.
		   myDatabase.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{DATABASE_TABLE});
		   
		}
	
	
	public Cursor getAllRows(){
		String where = null;
		Cursor c = myDatabase.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if(c != null){
			c.moveToFirst();
			
		}
		return c;
		
	}
    public Cursor Recent(){
        String last5 = "SELECT * FROM menes ORDER BY _id DESC LIMIT 5;";

        Cursor c = myDatabase.rawQuery(last5, null);
        if(c != null){
            c.moveToFirst();

        }
        return c;

    }


    public Cursor Random(){
        String last5 = "SELECT * FROM menes ORDER BY RANDOM() LIMIT 5;";
        Cursor c = myDatabase.rawQuery(last5, null);
        if(c != null){
            c.moveToFirst();

        }
        return c;

    }


    public Cursor Query(String queryy){
        String query = "SELECT * FROM menes WHERE tag1 LIKE '%" + queryy+"%' OR tag2 LIKE '%" + queryy+"%' OR tag3 LIKE '%" + queryy+"%' ;";
        //String query = "SELECT * FROM menes WHERE tag1,tag2,tag3 LIKE '%" + queryy+"';";
        Log.i("SCRIPT",query);

        Cursor c = myDatabase.rawQuery(query, null);
        if(c != null){
            c.moveToFirst();

        }
        return c;

    }
	
	
	
}