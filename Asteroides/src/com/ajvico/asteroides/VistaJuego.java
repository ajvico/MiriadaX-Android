package com.ajvico.asteroides;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Vista que permite mostrar todos los elementos del juego.
 */
public class VistaJuego
   extends View
{
   /* NAVE */

   /**
    * Gráfico de la nave.
    */
   private Grafico nave;

   /**
    * Incremento de dirección.
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

   //
   /**
    * Fragmentos en que se divide cada asteroide.
    */
   private int numFragmentos = 3;


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
      drawableAsteroide =
         context.getResources().getDrawable(R.drawable.asteroide1);

      // Creamos el elemento gráfico para la nave.
      nave = new Grafico(this, drawableNave);

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
   }


   /**
    * Método que se llama cuando se tiene que dibujar la vista.
    * 
    * @see android.view.View#onDraw(android.graphics.Canvas)
    */
   @Override
   protected void onDraw(Canvas canvas)
   {
      super.onDraw(canvas);

      // Mostramos la nave
      nave.dibujaGrafico(canvas);

      // Mostramos los asteroides
      for (Grafico asteroide : asteroides)
      {
         asteroide.dibujaGrafico(canvas);
      }
   }
}