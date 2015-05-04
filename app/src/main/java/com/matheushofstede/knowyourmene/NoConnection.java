package com.matheushofstede.knowyourmene;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.Random;

/**
 * Created by he4dless on 05/04/15.
 */
public class NoConnection extends Activity {

    boolean status = false;
    final String[] error_message = {"Falha ao sumonar os menes, verifique a conexão com a internet (coloque crédito)", "Que tal não roubar a internet do vizinho?", "Sua operadora é Tim?", "Ta no meio do mato parça?", "Não posso fazer nada por vc...", "Sua internet é tão ruim que vou chamar de iphone", "Meça suas frases de erro parça", "Leve essas frases na brincadeira, serio", "Me siga no Google Cátion!"};
    TextView titulo, randomt;
    ImageView carinha;
    FloatingActionButton fabe;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noconnectionactivity);
        status = checkInternetConnection();
        titulo = (TextView)findViewById(R.id.titulo);
        randomt = (TextView)findViewById(R.id.random);
        Random random = new Random();
        index = random.nextInt(error_message.length);
        randomt.setText(error_message[index]);
        carinha = (ImageView)findViewById(R.id.carinha);
        fabe = (FloatingActionButton)findViewById(R.id.fabe);
        fabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();

                if (status)
                {
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    //iniciar a activity normal
                }
                else
                {
                    Random random = new Random();
                    index = random.nextInt(error_message.length);
                    randomt.setText(error_message[index]);
                    checkInternetConnection();
                }

            }
        });



    }



    private boolean checkInternetConnection() {
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] inf = connectivity.getAllNetworkInfo();
            if (inf != null)
                for (int i = 0; i < inf.length; i++)
                    if (inf[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
