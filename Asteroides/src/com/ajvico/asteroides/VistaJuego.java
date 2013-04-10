package com.ajvico.asteroides;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Vista que permite mostrar todos los elementos del juego.
 */
public class VistaJuego
   extends View
   implements SensorEventListener
{
   /* NAVE */

   /**
    * Gr�fico de la nave.
    */
   private Grafico nave;

   /**
    * Velocidad de giro de la nave.
    */
   private int giroNave;

   /**
    * Aumento de velocidad.
    */
   private float aceleracionNave;

   /**
    * Incremento est�ndar de giro.
    */
   private static final int PASO_GIRO_NAVE = 5;

   /**
    * Aceleraci�n de la nave.
    */
   private static final float PASO_ACELERACION_NAVE = 0.5f;

   /* ASTEROIDES */

   /**
    * Vector con los asteroides.
    */
   private Vector<Grafico> asteroides;

   /**
    * N�mero inicial de asteroides.
    */
   private int numAsteroides = 5;

   /**
    * Fragmentos en que se divide cada asteroide.
    */
   private int numFragmentos = 3;

   /* MISIL */

   /**
    * Gr�fico del misil.
    */
   private Grafico misil;

   /**
    * Incremento de la velocidad del misil.
    */
   private static int PASO_VELOCIDAD_MISIL = 12;

   /**
    * Determina si tenemos un misil en pantalla o no.
    */
   private boolean misilActivo = false;

   /**
    * Tiempo que permanece como m�ximo un misil en pantalla.
    */
   private int tiempoMisil;

   /* CONTROL DEL JUEGO */

   /**
    * Coordenada X del �ltimo evento t�ctil.
    */
   private float mX = 0;

   /**
    * Coordenada Y del �ltimo evento t�ctil.
    */
   private float mY = 0;

   /**
    * Flag que controla el estado t�ctil asociado al disparo (pulsaci�n sin
    * desplazamiento)
    */
   private boolean disparo = false;

   /**
    * Flag que indica si he ha guardado ya una primera lectura del sensor.
    */
   private boolean hayValorInicial = false;

   /**
    * Valor obtenido del lector al crear la vista.
    */
   private float valorInicial;

   /**
    * Puntuaci�n del jugador.
    */
   private int puntuacion = 0;

   /* THREAD Y TIEMPO */

   /**
    * Thread encargado de procesar el juego.
    */
   private ThreadJuego thread = new ThreadJuego();

   /**
    * Cada cuanto queremos procesar cambios (ms).
    */
   private static int PERIODO_PROCESO = 40;

   /**
    * Cuando se realiz� el �ltimo proceso.
    */
   private long ultimoProceso = 0;

   /* SENSORES */

   /**
    * Gestor de sensores. Se usa para los controles del juego.
    */
   private SensorManager mSensorManager;

   /**
    * Lista de sensores de orientaci�n disponibles. La guardamos para no tener
    * que obtenerla cada vez.
    */
   private List<Sensor> listSensors;

   /* VARIOS */

   /**
    * Actividad que aloja esta vista.
    */
   private Activity padre;


   /**
    * Constructor de la vista.
    * Se encarga de crear todos los elementos del juego.
    * 
    * @param context
    *        Contexto en el que se incluye la vista (la actividad del juego).
    * @param attrs
    *        Atributos suministrados a la vista desde el archivo XML del layout
    *        d�nde se incluye.
    */
   public VistaJuego(
      Context context,
      AttributeSet attrs)
   {
      super(context, attrs);

      // Cargamos las im�genes de los elementos gr�ficos del juego a partir de
      // los recursos definidos
      Drawable drawableNave, drawableAsteroide, drawableMisil;
      drawableNave = context.getResources().getDrawable(R.drawable.nave);
      drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
      drawableAsteroide =
         context.getResources().getDrawable(R.drawable.asteroide1);

      // Creamos el elemento gr�fico para la nave.
      nave = new Grafico(this, drawableNave);

      // Creamos el elemento gr�fico para el misil
      misil = new Grafico(this, drawableMisil);

      // Creamos un array para albergar los elementos gr�ficos de los asteroides
      asteroides = new Vector<Grafico>();

      for (int i = 0; i < numAsteroides; i++)
      {
         // Creamos el elemento gr�fico del asteroide
         Grafico asteroide = new Grafico(this, drawableAsteroide);

         // Definimos su velocidad (lineal y de giro) y su �ngulo iniciales
         asteroide.setIncY(Math.random() * 4 - 2);
         asteroide.setIncX(Math.random() * 4 - 2);
         asteroide.setAngulo((int) (Math.random() * 360));
         asteroide.setRotacion((int) (Math.random() * 8 - 4));

         // A�adimos el elemento gr�fico al array de asteroides
         asteroides.add(asteroide);
      }

      // Obtenemos el gestor de sensores y los sensores de orientaci�n
      // disponibles
      mSensorManager =
         (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
      listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
   }


   /**
    * M�todo que se llama cuando el tama�o de la vista var�a.
    * Se proporciona el nuevo tama�o y el anterior.
    * Hasta que no se llama a este m�todo se desconoce el tama�o que tiene la
    * vista.
    * 
    * @see android.view.View#onSizeChanged(int, int, int, int)
    */
   @Override
   protected void onSizeChanged(
      int ancho,
      int alto,
      int ancho_anter,
      int alto_anter)
   {
      super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

      // Situamos la nave en el centro de la vista
      nave.setPosX((ancho - nave.getAncho()) / 2);
      nave.setPosY((alto - nave.getAlto()) / 2);

      // Situamos aleatoriamente cada asteroide
      for (Grafico asteroide : asteroides)
      {
         // Nos aseguramos de que el asteroide no queda demasiado cerca de la
         // nave.
         do
         {
            asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
            asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
         }
         while (asteroide.distancia(nave) < (ancho + alto) / 5);
      }

      // Iniciamos el hilo que controla los elementos del juego
      ultimoProceso = System.currentTimeMillis();
      thread.start();
   }


   /**
    * M�todo que se llama cuando se tiene que dibujar la vista.
    * 
    * @see android.view.View#onDraw(android.graphics.Canvas)
    */
   @Override
   protected synchronized void onDraw(Canvas canvas)
   {
      super.onDraw(canvas);

      // Mostramos la nave
      nave.dibujaGrafico(canvas);

      // Si tenemos un misil activo, lo mostramos
      if (misilActivo)
         misil.dibujaGrafico(canvas);

      // Mostramos los asteroides
      for (Grafico asteroide : asteroides)
      {
         asteroide.dibujaGrafico(canvas);
      }
   }


   /**
    * M�todo que se llama cuando se interact�a con la pantalla t�ctil.
    * 
    * @param event
    *        Evento que se ha genereado en la pantalla t�ctil
    * @return
    *         True si el evento se ha gestionado aqu�. False si debe seguir
    *         propag�ndose.
    * @see android.view.View#onTouchEvent(android.view.MotionEvent)
    */
   @Override
   public boolean onTouchEvent(MotionEvent event)
   {
      super.onTouchEvent(event);

      // Recuperamos las coordenadas en las que se ha producido el evento
      float x = event.getX();
      float y = event.getY();

      // TODO: Convertir los factores que adaptan la interacci�n con la pantalla
      // t�ctil en preferencias de la aplicaci�n

      // Comprobamos la acci�n que se ha detectado
      switch (event.getAction())
      {
         case MotionEvent.ACTION_DOWN:
            // Si se ha iniciado una pulsaci�n, por el momento consideramos un
            // posible disparo
            disparo = true;
            break;

         // Si se ha desplazado el dedo por la pantalla t�ctil hay que
         // determinar si se ha hecho de forma vertical u horizontal
         case MotionEvent.ACTION_MOVE:

            // Vemos las diferencias entre las coordenadas almacenadas (las de
            // la pulsaci�n que inici� el movimiento) y las actuales
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);

            // Si el movimiento se realiza en el eje X (horizontal)
            if (dy < 6 && dx > 6)
            {
               // Modificamos la velocidad de giro de la nave en funci�n del
               // movimiento
               giroNave = Math.round((x - mX) / 2);

               // Si hay desplazamiento del dedo, no es disparo
               disparo = false;
            }
            else if (dx < 6 && dy > 6) // Si el movimiento es vertical
            {
               // Modificamos la aceleraci�n de la nave en funci�n del
               // movimiento
               aceleracionNave = Math.round((mY - y) / 25);

               // Para impedir que se decelere, la aceleraci�n no puede ser
               // negativa
               // TODO: A�adir una preeferencia para activar/desactivar esto
               if (aceleracionNave < 0) aceleracionNave = 0;

               // Si hay desplazamiento del dedo, no es disparo
               disparo = false;
            }
            break;

         // Cuando el dedo se levanta de la pantalla, dejamos de girar o
         // acelerar la nave
         case MotionEvent.ACTION_UP:
            giroNave = 0;
            aceleracionNave = 0;

            // Si hemos detectado una pulsaci�n sin desplazamiento
            if (disparo)
            {
               // Disparamos
               ActivaMisil();
            }
            break;
      }

      // Guardamos las nuevas coordenadas
      mX = x;
      mY = y;

      // Devolvemos true para que el evento deje de propagarse
      return true;
   }


   /**
    * M�todo que se ejecuta si cambia la precisi�n del sensor.
    * 
    * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor,
    *      int)
    */
   @Override
   public void onAccuracyChanged(Sensor sensor, int accuracy)
   {
   }


   /**
    * M�todo que se ejecuta cada vez que alguno de los sensores registrados
    * produce alg�n eventos
    * 
    * @param event
    *        Evento que ha producido el sensor. Incluye los nuevos valores
    *        medidos.
    * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
    */
   @Override
   public void onSensorChanged(SensorEvent event)
   {
      // Recuperamos los valores medidos por el sensor
      float valor = event.values[1];

      // La primera vez guardamos el valor obtenido
      if (!hayValorInicial)
      {
         valorInicial = valor;
         hayValorInicial = true;
      }

      // Actualizamos el giro de la nave midiendo la diferencia entre el valor
      // actual y el que se midi� cuando se cre� la vista.
      giroNave = (int) (valorInicial - valor) / 3;
   }


   /**
    * Registra los sensores que se utilizan para controlar la nave.
    */
   public void registrarSensores()
   {
      if (!listSensors.isEmpty())
      {
         Sensor orientationSensor = listSensors.get(0);
         mSensorManager.registerListener(
            this,
            orientationSensor,
            SensorManager.SENSOR_DELAY_GAME);
      }
   }


   /**
    * Desregistra los seonsores que se utilizan para controlar de nave.
    * De esta forma dejan de hacerse mediciones.
    */
   public void desregistrarSensores()
   {
      mSensorManager.unregisterListener(this);
   }


   /**
    * Actualiza la posici�n de todos los elementos del juego.
    * Calcula la velocidad y direcci�n de la nave a partir de la informaci�n
    * proporcionada por el jugador mediante los controles del juego.
    */
   protected synchronized void actualizaFisica()
   {
      long ahora = System.currentTimeMillis();

      // No hagas nada si el per�odo de proceso no se ha cumplido.
      if (ultimoProceso + PERIODO_PROCESO > ahora)
      {
         return;
      }

      // Para una ejecuci�n en tiempo real calculamos retardo
      double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
      ultimoProceso = ahora; // Para la pr�xima vez

      // Actualizamos velocidad y direcci�n de la nave a partir de giroNave y
      // aceleracionNave (seg�n la entrada del jugador)
      nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
      double nIncX =
         nave.getIncX() + aceleracionNave
            * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
      double nIncY =
         nave.getIncY() + aceleracionNave
            * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;

      // Actualizamos si el m�dulo de la velocidad no excede el m�ximo
      if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad())
      {
         nave.setIncX(nIncX);
         nave.setIncY(nIncY);
      }

      // Actualizamos posiciones X e Y
      nave.incrementaPos(retardo);
      for (Grafico asteroide : asteroides)
      {
         asteroide.incrementaPos(retardo);
      }

      // Si tenemos un misil activo, actualizamos su posici�n
      if (misilActivo)
      {
         misil.incrementaPos(retardo);

         // Controlamos el tiempo que pasa activo el misil
         tiempoMisil -= retardo;
         if (tiempoMisil < 0)
         {
            // Si se agota el tiempo, el misil deja de estar activo
            misilActivo = false;
         }
         else
         {
            // Si el misil sigue activo, miramos si ha chocado con alg�n
            // asteroide
            for (int i = 0; i < asteroides.size(); i++)
               if (misil.verificaColision(asteroides.elementAt(i)))
               {
                  // Si el misil choca, destruimos el asteroide
                  destruyeAsteroide(i);
                  break;
               }
         }
      }

      // Comprobamos si alg�n asteroide ha chocado con la nave
      for (Grafico asteroide : asteroides)
      {
         if (asteroide.verificaColision(nave))
         {
            salir();
         }
      }
   }


   /**
    * Destruye uno de los asteroides (por haber sido alcanzado por un misil).
    * 
    * @param i
    *        N�mero del asteroide que hay que destruir.
    */
   private void destruyeAsteroide(int i)
   {
      // Eliminamos el asteroide de la lista de asteroides
      asteroides.remove(i);

      // Hacemos desaparecer el misil
      misilActivo = false;

      // Sumamos los puntos correspondientes a la destrucci�n del asteroide
      puntuacion += 1000;

      // Si ya no nos quedan m�s asteroides
      if (asteroides.isEmpty())
      {
         // Finalizamos el juego
         salir();
      }
   }


   private void ActivaMisil()
   {
      // Nos aseguramos que de el misil anterior (si existiera) se borra
      misil.invalidar();

      // Calculamos la posici�n de la que tiene que partir el misil
      misil.setPosX(
         nave.getPosX() + nave.getAncho() / 2 - misil.getAncho() / 2);
      misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);

      // Calculamos el �ngulo y la velocidad con la que debe salir el misil
      misil.setAngulo(nave.getAngulo());
      misil.setIncX(
         Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
      misil.setIncY(
         Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);

      // Fijamos el tiempo m�ximo que puede estar el misil en pantalla
      // El objetivo es impedir que cruce la pantalla e impacte en la propia
      // nave
      tiempoMisil =
         (int) Math.min(
            this.getWidth() / Math.abs(misil.getIncX()),
            this.getHeight() / Math.abs(misil.getIncY())) - 2;

      // Marcamos el misil como activo para que se muestre
      misilActivo = true;
   }


   /**
    * Finaliza la actividad que contiene a esta vista y devuelve la puntuaci�n
    * acumulada.
    */
   private void salir()
   {
      Bundle bundle = new Bundle();
      bundle.putInt("puntuacion", puntuacion);
      Intent intent = new Intent();
      intent.putExtras(bundle);
      padre.setResult(Activity.RESULT_OK, intent);
      padre.finish();
   }


   /**
    * Hilo para actualizar la posici�n de los elementos de juego en segundo
    * plano
    */
   class ThreadJuego
      extends Thread
   {

      /**
       * Flag que controla la pausa del hilo.
       */
      private boolean pausa;

      /**
       * Flag que controla la detenci�n del hilo.
       */
      private boolean corriendo;


      /**
       * Permite pausar temporalmente el hilo.
       */
      public synchronized void pausar()
      {
         pausa = true;
      }


      /**
       * Permite reanudar la ejecuci�n del hilo si est� pausada.
       */
      public synchronized void reanudar()
      {
         pausa = false;
         ultimoProceso = System.currentTimeMillis();
         notify();
      }


      /**
       * Permite detener por completo la ejecuci�n del hilo.
       */
      public void detener()
      {
         corriendo = false;
         if (pausa) reanudar();
      }


      @Override
      public void run()
      {
         // S�lo mantenemos la ejecuci�n del hilo mientras no lo paren
         // externamente
         corriendo = true;
         while (corriendo)
         {
            // Actualizamos la posici�n de los elementos del juego
            actualizaFisica();

            // Pausamos el hilo si nos lo piden desde fuera
            synchronized (this)
            {
               while (pausa)
               {
                  try
                  {
                     wait();
                  }
                  catch (Exception e)
                  {
                  }
               }
            }

            // Dormimos el hilo hasta que toque actualizar de nuevo
            try
            {
               Thread.sleep(PERIODO_PROCESO);
            }
            catch (InterruptedException e)
            {
               Thread.currentThread().interrupt();
            }
         }
      }
   } // class ThreadJuego


   /**
    * Permite obtener el hilo de ejecuci�n que actualiza la vista del juego.
    * 
    * @return El hilo de ejecuci�n
    */
   public ThreadJuego getThread()
   {
      return thread;
   }


   /**
    * Permite almacenar en la vista la actividad que la contiene.
    * 
    * @param padre
    *        La actividad que aloja esta vista.
    */
   public void setPadre(Activity padre)
   {
      this.padre = padre;
   }

} // class VistaJuego