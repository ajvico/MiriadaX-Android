package com.ajvico.asteroides;

import java.util.Vector;


/**
 * Interfaz para los almacenes de las puntuaciones del juego.
 */
public interface IAlmacenPuntuaciones
{
   /**
    * Método para guardar la puntuación de un jugador en la lista de
    * puntuaciones.
    * 
    * @param puntos
    *        Cantidad de puntos que ha obtenido el jugador.
    * @param nombre
    *        Nombre del jugador.
    * @param fecha
    *        Fecha en la que ha obtenido la puntuación.
    */
   public void guardarPuntuacion(int puntos, String nombre, long fecha);


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
    */
   public Vector<String> listaPuntuaciones(int cantidad);
}
