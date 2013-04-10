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
    *        Actividad a la que se asociará el archivo de preferencias.
    */
   public AlmacenPuntuacionesPreferencias(
      Context context)
   {
      this.context = context;
   }


   /**
    * Permite almacenar la última puntuación en el archivo de preferencias.
    * 
    * @param puntos
    *        Cantidad de puntos que ha obtenido el jugador.
    * @param nombre
    *        Nombre del jugador.
    * @param fecha
    *        Fecha en la que ha obtenido la puntuación.
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

      // Almacenamos la puntuación, sobrescribiendo a la que hubiera
      editor.putString("puntuacion", puntos + " " + nombre);
      editor.commit();
   }


   /**
    * Devuelve la última puntuación almacenada.
    * 
    * @param cantidad
    *        Cantidad de puntuaciones que queremos obtener. Aquí se ignora.
    * @return
    *         Lista con las puntuaciones solicitadas. Se devuelve una única
    *         puntuación como cadena de texto. Los puntos seguidos del nombre
    *         del jugador.
    * @see com.ajvico.asteroides.IAlmacenPuntuaciones#listaPuntuaciones(int)
    */
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      // Obtenemos las preferencias asociadas al contexto
      SharedPreferences preferencias =
         context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

      // Extraemos la puntuación de las preferencias (sólo se almacena una
      // puntuación)
      String s = preferencias.getString("puntuacion", "");

      // Si tenemos puntuación, la añadimos a la lista
      Vector<String> result = new Vector<String>();
      if (s != "")
      {
         result.add(s);
      }

      // Devolvemos la puntuación en forma de lista
      return result;
   }
}
