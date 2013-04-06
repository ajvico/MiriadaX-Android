package com.ajvico.asteroides;

import android.app.Activity;
import android.os.Bundle;


/**
 * Actividad para mostrar la acción del juego.
 */
public class Juego
   extends Activity
{
   private VistaJuego vistaJuego;


   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.juego);
      vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
   }


   @Override
   protected void onPause()
   {
      super.onPause();
      vistaJuego.desregistrarSensores();
      vistaJuego.getThread().pausar();
   }


   @Override
   protected void onResume()
   {
      super.onResume();
      vistaJuego.registrarSensores();
      vistaJuego.getThread().reanudar();
   }


   @Override
   protected void onDestroy()
   {
      vistaJuego.getThread().detener();
      super.onDestroy();
   }
}
