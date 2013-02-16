package com.ajvico.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Asteroides
   extends Activity
{
   // Referencia para el bot�n "Sobre el juego".
   // Se usa para a�adirle un escuchador para el evento onClick
   private Button bAcercaDe;


   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // Se a�ade un escuchador para el evento onClick del bot�n
      // "Sobre el juego"
      bAcercaDe = (Button) findViewById(R.id.btnInfo);
      bAcercaDe.setOnClickListener(new OnClickListener()
      {
         public void onClick(View view)
         {
            lanzarAcercaDe(view);
         }
      });

      // Se a�ade otro escuchador para el evento onClick del bot�n "Salir"
      ((Button) findViewById(R.id.btnSalir))
         .setOnClickListener(new OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               finish();
            }
         });
   }


   public void lanzarAcercaDe(View view)
   {
      Intent i = new Intent(this, AcercaDe.class);
      startActivity(i);
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true; // true -> el men� ya est� visible
   }


   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case R.id.acercaDe:
            lanzarAcercaDe(null);
            break;
      }
      return true; // true -> consumimos el item, no se propaga
   }
}
