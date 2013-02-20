package com.ajvico.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Actividad que permite modificar las opciones del juego.
 */
public class Preferencias
   extends PreferenceActivity
{
   /**
    * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(
      Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      // TODO: Añadir la implementación actual (con fragments), pero mantener
      // esta llamada para las versiones anteriores de Android. Ver:
      // http://stackoverflow.com/questions/6822319/what-to-use-instead-of-addpreferencesfromresource-in-a-preferenceactivity
      addPreferencesFromResource(R.xml.preferencias);
   }
}
