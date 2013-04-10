/**
 * 
 */
package com.ajvico.asteroides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;


public class ServicioMusica
   extends Service
{
   MediaPlayer reproductor;

   private NotificationManager nm;

   private static final int ID_NOTIFICACION_CREAR = 1;


   @Override
   public void onCreate()
   {
      //Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
      nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      reproductor = MediaPlayer.create(this, R.raw.audio);
   }


   @Override
   public int onStartCommand(Intent intenc, int flags, int idArranque)
   {
//      Toast.makeText(
//         this,
//         "Servicio arrancado " + idArranque,
//         Toast.LENGTH_SHORT).show();
      reproductor.start();

      // Creamos una notificaci�n, indicando icono, texto y cu�ndo se mostrar�
      Notification notificacion = new Notification(
         R.drawable.ic_launcher,
         "Creando Servicio de M�sica",
         System.currentTimeMillis());

      // Creamos una intenci�n para la actividad que se mostrar� al pulsar en la
      // notificaci�n
      PendingIntent intencionPendiente = PendingIntent.getActivity(
         this, 0, new Intent(this, Asteroides.class), 0);

      // A�adimos la informaci�n que se mostrar� la notificaci�n al desplegar la
      // barra de notificaciones
      notificacion.setLatestEventInfo(this, "Reproduciendo m�sica",
         "informaci�n adicional", intencionPendiente);

      // Enviamos la notificaci�n al gestor de notificaciones
      nm.notify(ID_NOTIFICACION_CREAR, notificacion);

      return START_STICKY;
   }


   // @Override
   // public void onStart(Intent intent, int startId)
   // {
   // Toast.makeText(this, "Servicio arrancado " + startId, Toast.LENGTH_SHORT)
   // .show();
   // reproductor.start();
   // }

   @Override
   public void onDestroy()
   {
      //Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
      reproductor.stop();
      
      // Al parar el servicio se elimina la notificaci�n
      nm.cancel(ID_NOTIFICACION_CREAR);
   }


   @Override
   public IBinder onBind(Intent intencion)
   {
      return null;
   }
}
