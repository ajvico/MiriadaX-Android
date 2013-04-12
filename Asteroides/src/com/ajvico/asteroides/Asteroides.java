package com.ajvico.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
   public static IAlmacenPuntuaciones almacen = null;

   /**
    * Referencia para el bot�n "Sobre el juego".
    * Se usa para a�adirle un escuchador para el evento onClick
    */
   private Button bAcercaDe;

   /**
    * Reproductor de audio.
    */
   private MediaPlayer mp;

   /**
    * Preferencias de la aplicaci�n.
    */
   SharedPreferences pref;


   /**
    * @see android.app.Activity#onCreate(android.os.Bundle)
    */
   @Override
   protected void onCreate(Bundle estadoGuardado)
   {
      super.onCreate(estadoGuardado);
      setContentView(R.layout.main);

      // Obtenemos las preferencias actuales
      pref = getSharedPreferences(
         "com.ajvico.asteroides_preferences",
         MODE_PRIVATE);

      // Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

      // Iniciamos el servicio que reproduce la m�sica
      // startService(new Intent(Asteroides.this, ServicioMusica.class));

      // Creamos el reproductor
      mp = MediaPlayer.create(this, R.raw.audio);

      // Si tenemos una posici�n guardada, la utilizamos
      if (estadoGuardado != null && mp != null)
      {
         int pos = estadoGuardado.getInt("posicion");
         mp.seekTo(pos);
      }

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
    * Crea un almac�n para la puntuaciones.
    * El tipo del almac�n se selecciona en las preferencias.
    */
   private void crearAlmacen()
   {
      int t_almacen = Integer.parseInt(pref.getString("almacen", "0"));
      switch (t_almacen)
      {
         case 0:
            almacen = new AlmacenPuntuacionesArray();
            break;

         case 1:
            almacen = new AlmacenPuntuacionesPreferencias(this);
            break;

         case 2:
            almacen = new AlmacenPuntuacionesFicheroInterno(this);
            break;
            
         case 3:
            almacen = new AlmacenPuntuacionesFicheroExterno(this);
            break;

         default:
            almacen = new AlmacenPuntuacionesArray();
      }
   }


   @Override
   protected void onStart()
   {
      super.onStart();
      // Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
   }


   @Override
   protected void onResume()
   {
      // Iniciamos o reanudamos la m�sica
      // Pero s�lo si la opci�n est� activa
      if (pref.getBoolean("musica", false))
      {
         mp.start();
      }

      // Creamos el almac�n en funci�n de las preferencias
      crearAlmacen();

      super.onResume();
      // Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
   }


   @Override
   protected void onPause()
   {
      // Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
      super.onPause();
   }


   @Override
   protected void onStop()
   {
      // Paramos la m�sica
      mp.pause();

      // Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
      super.onStop();
   }


   @Override
   protected void onSaveInstanceState(Bundle estadoGuardado)
   {
      super.onSaveInstanceState(estadoGuardado);
      if (mp != null)
      {
         int pos = mp.getCurrentPosition();
         estadoGuardado.putInt("posicion", pos);
      }
   }


   @Override
   protected void onRestart()
   {
      super.onRestart();
      // Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
   }


   @Override
   protected void onDestroy()
   {
      // Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

      // Paramos el servicio que reproduce la m�sica
      // stopService(new Intent(Asteroides.this, ServicioMusica.class));

      super.onDestroy();
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
      // startActivity(i);
      startActivityForResult(i, 1234);
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


   /**
    * M�todo que se ejecuta cuando una actividad lanzada desde esta devuelve un
    * valor.
    * 
    * @param requestCode
    *        Identificador enviado al crear el intent que lanz� la actividad que
    *        est� respondiendo.
    * @param resultCode
    *        C�digo de resultado devuelto por la actividad.
    * @param data
    *        Datos adicionales devueltos por la actividad.
    * @see android.app.Activity#onActivityResult(int, int,
    *      android.content.Intent)
    */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);

      // Comprobamos que la respuesta es de la actividad que lanzamos, que ha
      // finalizado correctamente y que ha devuelto datos
      if (requestCode == 1234 & resultCode == RESULT_OK & data != null)
      {
         // Recuperamos la puntuaci�n obtenida por el jugador
         int puntuacion = data.getExtras().getInt("puntuacion");

         // Mejor leerlo desde un Dialog o una nueva actividad
         // AlertDialog.Builder
         String nombre = "Yo";

         // Guardamos la puntuaci�n obtenida en el almazac�n de puntuaciones
         if (almacen != null)
         {
            almacen.guardarPuntuacion(
               puntuacion,
               nombre,
               System.currentTimeMillis());
         }

         // Mostramos la lista de puntuaciones actual
         lanzarPuntuaciones(null);
      }
   }
}
