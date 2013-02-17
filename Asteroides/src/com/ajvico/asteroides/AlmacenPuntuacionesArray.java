package com.ajvico.asteroides;

import java.util.Vector;


public class AlmacenPuntuacionesArray
   implements IAlmacenPuntuaciones
{
   private Vector<String> puntuaciones;


   public AlmacenPuntuacionesArray()
   {
      puntuaciones = new Vector<String>();
      
      // Metemos algunos datos falsos para tener algo que mostrar
      puntuaciones.add("123000 Pepito Domingez");
      puntuaciones.add("111000 Pedro Martinez");
      puntuaciones.add("011000 Paco Pérez");
   }


   @Override
   public void guardarPuntuacion(int puntos, String nombre, long fecha)
   {
      puntuaciones.add(0, puntos + " " + nombre);
   }


   @Override
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      return puntuaciones;
   }
}
