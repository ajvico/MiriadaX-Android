package com.ajvico.asteroides;

import java.util.Vector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MiAdaptador
   extends BaseAdapter
{
   // Actividad a la que se asocia el adaptador
   private final Activity actividad;

   // Lista de puntuaciones y nombres que se mostrarán en la actividad
   private final Vector<String> lista;


   /**
    * Constructor del adaptador.
    * Requiere la actividad que contiene el ListView y la lista de cadenas a
    * mostrar.
    * 
    * @param actividad
    *        Actividad en la que se muestra la lista de puntuaciones.
    * @param lista
    *        Lista de puntuaciones y nombres que se mostrará.
    */
   public MiAdaptador(
      Activity actividad,
      Vector<String> lista)
   {
      super();
      this.actividad = actividad;
      this.lista = lista;
   }


   /**
    * Contruye cada elemento de la lista de puntuaciones.
    * 
    * @param position
    *        Posición del elemento a construir en la lista de puntuaciones.
    * @param convertView
    *        Vista base para los elementos a construir.
    * @param parent
    *        ListView en el que se incluirá la vista devuelta.
    * @return
    *         Una vista con la puntuación de un usuario.
    * @see android.widget.Adapter#getView(int, android.view.View,
    *      android.view.ViewGroup)
    */
   public View getView(int position, View convertView, ViewGroup parent)
   {
      // Obtenemos el creador de diseños de la actividad asociada al adaptador
      LayoutInflater inflater = actividad.getLayoutInflater();

      // Creamos un diseño a partir del contenido de elemento_lista.xml
      View view = inflater.inflate(R.layout.elemento_lista, null, true);

      // Accedemos al TextView del diseño de cada elemento
      TextView textView = (TextView) view.findViewById(R.id.titulo);

      // Asignamos la puntuación y el nombre del usuario que corresponde
      textView.setText(lista.elementAt(position));

      // Accedemos al ImageView que mostrará el icono
      ImageView imageView = (ImageView) view.findViewById(R.id.icono);

      // Se añade un icono de forma aleatoria
      switch (Math.round((float) Math.random() * 3))
      {
         case 0:
            imageView.setImageResource(
               R.drawable.asteroide1);
            break;
         case 1:
            imageView.setImageResource(
               R.drawable.asteroide2);
            break;
         default:
            imageView.setImageResource(
               R.drawable.asteroide3);
            break;
      }

      // Devolvemos la vista construida
      return view;
   }


   /**
    * Permite obtener la cantidad de elementos que tendrá el ListView.
    * 
    * @return
    *         El número de puntuaciones que tenemos en la lista.
    * @see android.widget.Adapter#getCount()
    */
   public int getCount()
   {
      return lista.size();
   }


   /**
    * Permite obtener los elementos de la lista de forma individual.
    * 
    * @param arg0
    *        Identificador de un elmento de la lista.
    * @return
    *         La puntuación y el nombre del usuario para el identificador
    *         especificado.
    * @see android.widget.Adapter#getItem(int)
    */
   public Object getItem(int arg0)
   {
      return lista.elementAt(arg0);
   }


   /**
    * Permite obtener un identificador único de un elemento de la lista.
    * 
    * @param position
    *        Posición del elemento que se desea identificar.
    * @return
    *         Identificador único del elemento de la posición especificada.
    * @see android.widget.Adapter#getItemId(int)
    */
   public long getItemId(int position)
   {
      return position;
   }
}
