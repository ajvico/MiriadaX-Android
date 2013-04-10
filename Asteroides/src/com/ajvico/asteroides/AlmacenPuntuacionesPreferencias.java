/**
 * 
 */
package com.ajvico.asteroides;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;


public class AlmacenPuntuacionesPreferencias
   implements IAlmacenPuntuaciones
{
   /**
    * Nombre del archivo de preferencias
    */
   private static String PREFERENCIAS = "puntuaciones";

   /**
    * Actividad a la que se asocia el archivo de preferencias.
    */
   private Context context;


   /**
    * Constructor de la clase.
    * 
    * @param context
    *        Actividad a la que se asociar� el archivo de preferencias.
    */
   public AlmacenPuntuacionesPreferencias(
      Context context)
   {
      this.context = context;
   }


   /**
    * Permite almacenar la �ltima puntuaci�n en el archivo de preferencias.
    * 
    * @param puntos
    *        Cantidad de puntos que ha obtenido el jugador.
    * @param nombre
    *        Nombre del jugador.
    * @param fecha
    *        Fecha en la que ha obtenido la puntuaci�n.
    * @see com.ajvico.asteroides.IAlmacenPuntuaciones#guardarPuntuacion(int,
    *      java.lang.String, long)
    */
   public void guardarPuntuacion(int puntos, String nombre, long fecha)
   {
      // Obtenemos las preferencias asociadas al contexto
      SharedPreferences preferencias =
         context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

      // Obtenemos el editor del archivo de preferencias
      SharedPreferences.Editor editor = preferencias.edit();

      // Almacenamos la puntuaci�n, sobrescribiendo a la que hubiera
      editor.putString("puntuacion", puntos + " " + nombre);
      editor.commit();
   }


   /**
    * Devuelve la �ltima puntuaci�n almacenada.
    * 
    * @param cantidad
    *        Cantidad de puntuaciones que queremos obtener. Aqu� se ignora.
    * @return
    *         Lista con las puntuaciones solicitadas. Se devuelve una �nica
    *         puntuaci�n como cadena de texto. Los puntos seguidos del nombre
    *         del jugador.
    * @see com.ajvico.asteroides.IAlmacenPuntuaciones#listaPuntuaciones(int)
    */
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      // Obtenemos las preferencias asociadas al contexto
      SharedPreferences preferencias =
         context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

      // Extraemos la puntuaci�n de las preferencias (s�lo se almacena una
      // puntuaci�n)
      String s = preferencias.getString("puntuacion", "");

      // Si tenemos puntuaci�n, la a�adimos a la lista
      Vector<String> result = new Vector<String>();
      if (s != "")
      {
         result.add(s);
      }

      // Devolvemos la puntuaci�n en forma de lista
      return result;
   }
}
