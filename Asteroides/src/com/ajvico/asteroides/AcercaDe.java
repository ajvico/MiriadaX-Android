package com.ajvico.asteroides;

import android.app.Activity;
import android.os.Bundle;


/**
 * Actividad que muestra información general sobre el juego.
 */
public class AcercaDe
   extends Activity
{
   /**
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.acercade);
   }
}
