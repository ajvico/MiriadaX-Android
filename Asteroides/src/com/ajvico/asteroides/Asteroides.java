package com.ajvico.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Asteroides
   extends Activity
{
   // Referencia para el botón "Sobre el juego".
   // Se usa para añadirle un escuchador para el evento onClick
   private Button bAcercaDe;


   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // Se añade un escuchador para el evento onClick del botón "Sobre el juego"
      bAcercaDe = (Button) findViewById(R.id.btnInfo);
      bAcercaDe.setOnClickListener(new OnClickListener()
      {
         public void onClick(View view)
         {
            lanzarAcercaDe(view);
         }
      });
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }


   public void lanzarAcercaDe(View view)
   {
      Intent i = new Intent(this, AcercaDe.class);
      startActivity(i);
   }
}
