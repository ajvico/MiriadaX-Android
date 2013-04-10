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
    * Gráfico de la nave.
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
    * Incremento estándar de giro.
    */
   private static final int PASO_GIRO_NAVE = 5;

   /**
    * Aceleración de la nave.
    */
   private static final float PASO_ACELERACION_NAVE = 0.5f;

   /* ASTEROIDES */

   /**
    * Vector con los asteroides.
    */
   private Vector<Grafico> asteroides;

   /**
    * Número inicial de asteroides.
    */
   private int numAsteroides = 5;

   /**
    * Fragmentos en que se divide cada asteroide.
    */
   private int numFragmentos = 3;

   /* MISIL */

   /**
    * Gráfico del misil.
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
    * Tiempo que permanece como máximo un misil en pantalla.
    */
   private int tiempoMisil;

   /* CONTROL DEL JUEGO */

   /**
    * Coordenada X del último evento táctil.
    */
   private float mX = 0;

   /**
    * Coordenada Y del último evento táctil.
    */
   private float mY = 0;

   /**
    * Flag que controla el estado táctil asociado al disparo (pulsación sin
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
    * Puntuación del jugador.
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
    * Cuando se realizó el último proceso.
    */
   private long ultimoProceso = 0;

   /* SENSORES */

   /**
    * Gestor de sensores. Se usa para los controles del juego.
    */
   private SensorManager mSensorManager;

   /**
    * Lista de sensores de orientación disponibles. La guardamos para no tener
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
    *        dónde se incluye.
    */
   public VistaJuego(
      Context context,
      AttributeSet attrs)
   {
      super(context, attrs);

      // Cargamos las imágenes de los elementos gráficos del juego a partir de
      // los recursos definidos
      Drawable drawableNave, drawableAsteroide, drawableMisil;
      drawableNave = context.getResources().getDrawable(R.drawable.nave);
      drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
      drawableAsteroide =
         context.getResources().getDrawable(R.drawable.asteroide1);

      // Creamos el elemento gráfico para la nave.
      nave = new Grafico(this, drawableNave);

      // Creamos el elemento gráfico para el misil
      misil = new Grafico(this, drawableMisil);

      // Creamos un array para albergar los elementos gráficos de los asteroides
      asteroides = new Vector<Grafico>();

      for (int i = 0; i < numAsteroides; i++)
      {
         // Creamos el elemento gráfico del asteroide
         Grafico asteroide = new Grafico(this, drawableAsteroide);

         // Definimos su velocidad (lineal y de giro) y su ángulo iniciales
         asteroide.setIncY(Math.random() * 4 - 2);
         asteroide.setIncX(Math.random() * 4 - 2);
         asteroide.setAngulo((int) (Math.random() * 360));
         asteroide.setRotacion((int) (Math.random() * 8 - 4));

         // Añadimos el elemento gráfico al array de asteroides
         asteroides.add(asteroide);
      }

      // Obtenemos el gestor de sensores y los sensores de orientación
      // disponibles
      mSensorManager =
         (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
      listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
   }


   /**
    * Método que se llama cuando el tamaño de la vista varía.
    * Se proporciona el nuevo tamaño y el anterior.
    * Hasta que no se llama a este método se desconoce el tamaño que tiene la
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
    * Método que se llama cuando se tiene que dibujar la vista.
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
    * Método que se llama cuando se interactúa con la pantalla táctil.
    * 
    * @param event
    *        Evento que se ha genereado en la pantalla táctil
    * @return
    *         True si el evento se ha gestionado aquí. False si debe seguir
    *         propagándose.
    * @see android.view.View#onTouchEvent(android.view.MotionEvent)
    */
   @Override
   public boolean onTouchEvent(MotionEvent event)
   {
      super.onTouchEvent(event);

      // Recuperamos las coordenadas en las que se ha producido el evento
      float x = event.getX();
      float y = event.getY();

      // TODO: Convertir los factores que adaptan la interacción con la pantalla
      // táctil en preferencias de la aplicación

      // Comprobamos la acción que se ha detectado
      switch (event.getAction())
      {
         case MotionEvent.ACTION_DOWN:
            // Si se ha iniciado una pulsación, por el momento consideramos un
            // posible disparo
            disparo = true;
            break;

         // Si se ha desplazado el dedo por la pantalla táctil hay que
         // determinar si se ha hecho de forma vertical u horizontal
         case MotionEvent.ACTION_MOVE:

            // Vemos las diferencias entre las coordenadas almacenadas (las de
            // la pulsación que inició el movimiento) y las actuales
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);

            // Si el movimiento se realiza en el eje X (horizontal)
            if (dy < 6 && dx > 6)
            {
               // Modificamos la velocidad de giro de la nave en función del
               // movimiento
               giroNave = Math.round((x - mX) / 2);

               // Si hay desplazamiento del dedo, no es disparo
               disparo = false;
            }
            else if (dx < 6 && dy > 6) // Si el movimiento es vertical
            {
               // Modificamos la aceleración de la nave en función del
               // movimiento
               aceleracionNave = Math.round((mY - y) / 25);

               // Para impedir que se decelere, la aceleración no puede ser
               // negativa
               // TODO: Añadir una preeferencia para activar/desactivar esto
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

            // Si hemos detectado una pulsación sin desplazamiento
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
    * Método que se ejecuta si cambia la precisión del sensor.
    * 
    * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor,
    *      int)
    */
   @Override
   public void onAccuracyChanged(Sensor sensor, int accuracy)
   {
   }


   /**
    * Método que se ejecuta cada vez que alguno de los sensores registrados
    * produce algún eventos
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
      // actual y el que se midió cuando se creó la vista.
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
    * Actualiza la posición de todos los elementos del juego.
    * Calcula la velocidad y dirección de la nave a partir de la información
    * proporcionada por el jugador mediante los controles del juego.
    */
   protected synchronized void actualizaFisica()
   {
      long ahora = System.currentTimeMillis();

      // No hagas nada si el período de proceso no se ha cumplido.
      if (ultimoProceso + PERIODO_PROCESO > ahora)
      {
         return;
      }

      // Para una ejecución en tiempo real calculamos retardo
      double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
      ultimoProceso = ahora; // Para la próxima vez

      // Actualizamos velocidad y dirección de la nave a partir de giroNave y
      // aceleracionNave (según la entrada del jugador)
      nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
      double nIncX =
         nave.getIncX() + aceleracionNave
            * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
      double nIncY =
         nave.getIncY() + aceleracionNave
            * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;

      // Actualizamos si el módulo de la velocidad no excede el máximo
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

      // Si tenemos un misil activo, actualizamos su posición
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
            // Si el misil sigue activo, miramos si ha chocado con algún
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

      // Comprobamos si algún asteroide ha chocado con la nave
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
    *        Número del asteroide que hay que destruir.
    */
   private void destruyeAsteroide(int i)
   {
      // Eliminamos el asteroide de la lista de asteroides
      asteroides.remove(i);

      // Hacemos desaparecer el misil
      misilActivo = false;

      // Sumamos los puntos correspondientes a la destrucción del asteroide
      puntuacion += 1000;

      // Si ya no nos quedan más asteroides
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

      // Calculamos la posición de la que tiene que partir el misil
      misil.setPosX(
         nave.getPosX() + nave.getAncho() / 2 - misil.getAncho() / 2);
      misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);

      // Calculamos el ángulo y la velocidad con la que debe salir el misil
      misil.setAngulo(nave.getAngulo());
      misil.setIncX(
         Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
      misil.setIncY(
         Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);

      // Fijamos el tiempo máximo que puede estar el misil en pantalla
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
    * Finaliza la actividad que contiene a esta vista y devuelve la puntuación
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
    * Hilo para actualizar la posición de los elementos de juego en segundo
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
       * Flag que controla la detención del hilo.
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
       * Permite reanudar la ejecución del hilo si está pausada.
       */
      public synchronized void reanudar()
      {
         pausa = false;
         ultimoProceso = System.currentTimeMillis();
         notify();
      }


      /**
       * Permite detener por completo la ejecución del hilo.
       */
      public void detener()
      {
         corriendo = false;
         if (pausa) reanudar();
      }


      @Override
      public void run()
      {
         // Sólo mantenemos la ejecución del hilo mientras no lo paren
         // externamente
         corriendo = true;
         while (corriendo)
         {
            // Actualizamos la posición de los elementos del juego
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
    * Permite obtener el hilo de ejecución que actualiza la vista del juego.
    * 
    * @return El hilo de ejecución
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