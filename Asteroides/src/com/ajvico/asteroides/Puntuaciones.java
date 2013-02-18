package com.ajvico.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class Puntuaciones
   extends ListActivity
{
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.puntuaciones);
      setListAdapter(new MiAdaptador(
         this,
         Asteroides.almacen.listaPuntuaciones(10)));
   }


   /**
    * Se ejecuta cuando se pulsa sobre un elemento del ListView que contiene
    * esta actividad.
    * 
    * @param listView
    *        Lista sobre la que se ha pulsado.
    * @param view
    *        Elemento de la lista sobre el que se ha pulsado.
    * @param position
    *        Posición que ocupa el elemento sobre el que se ha pulsado en la
    *        lista.
    * @param id
    *        Identificador del elemento sobre el que se ha pulsado
    * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
    *      android.view.View, int, long)
    */
   @Override
   protected void onListItemClick(
      ListView listView,
      View view,
      int position,
      long id)
   {
      // Llamamos al método de la clase padre
      super.onListItemClick(listView, view, position, id);

      // Obtenemos el texto asociado al elemento sobre el que se ha pulsado
      Object o = getListAdapter().getItem(position);

      // Creamos un mensaje informativo con los datos del elemento sobre el que
      // se ha pulsado y lo mostramos
      Toast.makeText(
         this,
         getResources().getString(R.string.textoSeleccionPuntuacion) + ": "
            + Integer.toString(position) + " - " + o.toString(),
         Toast.LENGTH_LONG).show();
   }
}
