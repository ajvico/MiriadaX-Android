package com.ajvico.asteroides;

import android.app.Activity;
import android.os.Bundle;


/**
 * Actividad para mostrar la acción del juego.
 */
public class Juego
   extends Activity
{
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.juego);
   }
}
