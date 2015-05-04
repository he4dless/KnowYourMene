package com.matheushofstede.knowyourmene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Created by he4dless on 24/02/15.
 */
public class UserSettingActivity extends PreferenceActivity
{
    public static MainActivity activity = new MainActivity();
    PostAsync main = new PostAsync();
    Context context = this;
    int test = 0;
    public static final String PREFS_NAME = "com.matheushofstede.knowyourmene_preferences";
    boolean firsttime;
    SharedPreferences sp;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
        Preference button = (Preference)findPreference("button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sp = getSharedPreferences(PREFS_NAME,0);





                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("firsttime",true);
                editor.commit();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);








                overridePendingTransition(0, 0);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();


                //new PostAsync().execute();
               //test++;
                //Toast.makeText(context, "Primeira vez que o app é aberta", Toast.LENGTH_LONG).show();
                //main.execute();
                return false;
            }
        });



    }

}