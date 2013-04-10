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

      // Desplazamos las puntuaciones almacenadas, descartando la más antigua
      for (int n = 9; n >= 1; n--)
      {
         editor.putString(
            "puntuacion" + n,
            preferencias.getString("puntuacion" + (n - 1), ""));
      }

      // Almacenamos la nueva puntuación, como la primera de la lista
      editor.putString("puntuacion0", puntos + " " + nombre);

      // Guardamos los cambios
      editor.commit();
   }


   /**
    * Devuelve la última puntuación almacenada.
    * 
    * @param cantidad
    *        Cantidad de puntuaciones que queremos obtener
    * @return
    *         Lista con las puntuaciones solicitadas. Se devuelven como cadena
    *         de texto. Los puntos seguidos del nombre
    *         del jugador.
    * @see com.ajvico.asteroides.IAlmacenPuntuaciones#listaPuntuaciones(int)
    */
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      // Obtenemos las preferencias asociadas al contexto
      SharedPreferences preferencias =
         context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
      
      // No podemos devolver más de 10 puntuaciones
      int num = (cantidad > 10 || cantidad < 0) ? 10 : cantidad;

      // Extraemos las puntuaciones de las preferencias
      Vector<String> result = new Vector<String>();
      for (int n = 0; n <= (num - 1); n++)
      {
         String s = preferencias.getString("puntuacion" + n, "");
         if (s != "")
         {
            result.add(s);
         }
      }

      // Devolvemos las puntuaciones
      return result;
   }
}
