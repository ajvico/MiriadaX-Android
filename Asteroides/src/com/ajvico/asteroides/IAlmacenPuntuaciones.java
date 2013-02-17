package com.ajvico.asteroides;

import java.util.Vector;


public interface IAlmacenPuntuaciones
{
   public void guardarPuntuacion(int puntos, String nombre, long fecha);


   public Vector<String> listaPuntuaciones(int cantidad);
}
