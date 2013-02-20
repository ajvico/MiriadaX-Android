package com.ajvico.asteroides;

import java.util.Vector;


/**
 * Clase que permite almacenar las puntuaciones del juego en memoria.
 */
public class AlmacenPuntuacionesArray
   implements IAlmacenPuntuaciones
{
   /**
    * Vector para guardar las puntuaciones en memoria.
    */
   private Vector<String> puntuaciones;


   /**
    * Crea un nuevo array para guardar las puntuaciones en memoria.
    */
   public AlmacenPuntuacionesArray()
   {
      puntuaciones = new Vector<String>();

      // Metemos algunos datos falsos para tener algo que mostrar
      puntuaciones.add("123000 Pepito Domingez");
      puntuaciones.add("111000 Pedro Martinez");
      puntuaciones.add("011000 Paco Pérez");
   }


   /**
    * Método para guardar la puntuación de un jugador en el array de
    * puntuaciones.
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
   @Override
   public void guardarPuntuacion(int puntos, String nombre, long fecha)
   {
      puntuaciones.add(0, puntos + " " + nombre);
   }


   /**
    * Método para obtener la lista de puntuaciones actual.
    * 
    * @param cantidad
    *        Cantidad de puntuaciones que queremos obtener. Permite limitar el
    *        tamaño de la lista devuelta.
    * @return
    *         Lista con las puntuaciones solicitadas. Cada puntuación se
    *         devuelve como cadena de texto, con los puntos y el nombre del
    *         jugador.
    * @see com.ajvico.asteroides.IAlmacenPuntuaciones#listaPuntuaciones(int)
    */
   @Override
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      // TODO: limitar la cantidad de puntuaciones devueltas
      return puntuaciones;
   }
}
