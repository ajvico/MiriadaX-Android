package com.ajvico.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * Actividad principal del juego.
 * Contiene el men� principal, que da acceso al resto de actividades del juego.
 */
public class Asteroides
   extends Activity
{
   /**
    * Variable de clase para guardar la lista de puntuaciones.
    */
   public static IAlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

   /**
    * Referencia para el bot�n "Sobre el juego".
    * Se usa para a�adirle un escuchador para el evento onClick
    */
   private Button bAcercaDe;


   /**
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // Se a�ade un escuchador para el evento onClick del bot�n
      // "Sobre el juego"
      bAcercaDe = (Button) findViewById(R.id.btnInfo);
      bAcercaDe.setOnClickListener(new OnClickListener()
      {
         public void onClick(View view)
         {
            lanzarAcercaDe(view);
         }
      });

      // Se a�ade otro escuchador para el evento onClick del bot�n "Salir"
      ((Button) findViewById(R.id.btnPuntuaciones))
         .setOnClickListener(new OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               lanzarPuntuaciones(v);
            }
         });

      // Se a�ade otro escuchador para el evento onClick del bot�n "Opciones"
      ((Button) findViewById(R.id.btnConfigurar))
         .setOnClickListener(new OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               lanzarPreferencias(v);
            }
         });

      // Se a�ade otro escuchador para el evento onClick del bot�n "Jugar"
      ((Button) findViewById(R.id.btnJugar))
         .setOnClickListener(new OnClickListener()
         {
            @Override
            public void onClick(View v)
            {
               lanzarJuego(v);
            }
         });
   }


   /**
    * Lanza la actividad Juego.
    * 
    * @param view
    *        Vista que ha generado el evento para lanzar la actividad.
    */
   public void lanzarJuego(View view)
   {
      Intent i = new Intent(this, Juego.class);
      startActivity(i);
   }


   /**
    * Lanza la actividad AcercaDe.
    * 
    * @param view
    *        Vista que ha generado el evento para lanzar la actividad.
    */
   public void lanzarAcercaDe(View view)
   {
      Intent i = new Intent(this, AcercaDe.class);
      startActivity(i);
   }


   /**
    * Lanza la actividad Preferencias.
    * 
    * @param view
    *        Vista que ha generado el evento para lanzar la actividad.
    */
   public void lanzarPreferencias(View view)
   {
      Intent i = new Intent(this, Preferencias.class);
      startActivity(i);
   }


   /**
    * Lanza la actividad Puntuaciones.
    * 
    * @param view
    *        Vista que ha generado el evento para lanzar la actividad.
    */
   public void lanzarPuntuaciones(View view)
   {
      Intent i = new Intent(this, Puntuaciones.class);
      startActivity(i);
   }


   /**
    * M�todo que se ejecuta cuando se va a mostrar el men� de la actividad.
    * Se encarga de crear el men�
    * 
    * @param menu
    *        Vista men� en la que hay que construir el men� que se mostrar�,
    * @return
    *         True si el men� se tiene que visualizar. False en caso
    *         contrario.
    * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
    */
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true; // true -> hay que visualizar el men�
   }


   /**
    * M�todo que se ejecuta cuando se selecciona una opci�n del men� de la
    * actividad.
    * Permite realizar la acci�n asociada a dicha opci�n del men�.
    * 
    * @param item
    *        Elemento del men� sobre el que se ha pulsado.
    * @return
    *         True si la opci�n del men� se procesa en este m�todo. False si se
    *         deja que sea el sistema el que la procese.
    * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
    */
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case R.id.acercaDe:
            lanzarAcercaDe(null);
            break;

         case R.id.config:
            lanzarPreferencias(null);
            break;

         case R.id.salir:
            // Cerramos la actividad. Como es la principal, se cerrar� toda la
            // aplicaci�n
            finish();
            break;
      }
      return true; // true -> consumimos el item, no se propaga
   }
}
