package com.ajvico.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;


/**
 * Clase privada para gestionar los elementos gráficos del juego.
 */
class Grafico
{
   /**
    * Imagen que dibujaremos.
    */
   private Drawable drawable;

   /**
    * Coordenada X del elemento gráfico.
    */
   private double posX;

   /**
    * Coordenada Y del elemento gráfico.
    */
   private double posY;

   /**
    * Velocidad de desplazamiento en el eje X.
    */
   private double incX;

   /**
    * Velocidad de desplazamiento en el eje Y.
    */
   private double incY;

   /**
    * Ánglulo de rotación.
    */
   private int angulo;

   /**
    * Velocidad de rotación.
    */
   private int rotacion;

   /**
    * Ancho de la imagen del elemento gráfico.
    */
   private int ancho;

   /**
    * Alto de la imagen del elemento gráfico.
    */
   private int alto;

   /**
    * Para determinar la colisión de un elemento gráfico con otro.
    */
   private int radioColision;

   /**
    * Dónde dibujamos el gráfico (usada en view.invalidate).
    */
   private View view;

   /**
    * Para determinar el espacio a borrar (view.invalidate).
    */
   public static final int MAX_VELOCIDAD = 20;


   /**
    * Constructor de la clase.
    * 
    * @param view
    *        Vista sobre la que dibujaremos el elemento gráfico.
    * @param drawable
    *        Imagen que hay que dibujar.
    */
   public Grafico(
      View view,
      Drawable drawable)
   {
      // Guardamos los parámetros recibidos en la clase
      this.view = view;
      this.drawable = drawable;

      // Obtenemos las dimensiones de la imagen a dibujar
      ancho = drawable.getIntrinsicWidth();
      alto = drawable.getIntrinsicHeight();

      // Calculamos el radio de colisión según el tamaño de la imagen
      radioColision = (alto + ancho) / 4;
   }


   /**
    * Método que se encarga de pintar el elemento gráfico en el lienzo
    * suministrado.
    * 
    * @param canvas
    *        Lienzo en el que se pintará el elemento gráfico.
    */
   public void dibujaGrafico(Canvas canvas)
   {
      // Guardamos la matriz y el área de recorte actuales
      canvas.save();

      // Obtenemos las coordenadas actuales del centro del elemento
      int x = (int) (posX + ancho / 2);
      int y = (int) (posY + alto / 2);

      // Rotamos el elemento sobre su centro, según el ángulo actual
      canvas.rotate((float) angulo, (float) x, (float) y);

      // Definimos el área dónde se dibujará el elemento gráfico, según sus
      // coordenadas actuales y sus dimensiones
      drawable.setBounds(
         (int) posX,
         (int) posY,
         (int) posX + ancho,
         (int) posY + alto);

      // Dibujamos el elemento gráfico en el lienzo proporcionado
      drawable.draw(canvas);

      // Restauramos la matriz y el área de recorte del lienzo para que los
      // ajustes realizados en este elemento gráfico no afecten al resto de
      // cosas que se pinten en dicho lienzo
      canvas.restore();

      // Calculamos el área máxima que puede ocupar el elemento gráfico al
      // desplazarse
      int rInval = (int) Math.hypot(ancho, alto) / 2 + MAX_VELOCIDAD;

      // Forzamos a que se vuelva a dibujar el área máxima que se ha podido ver
      // afectada por el movimiento que ha realizado el elemento gráfico
      view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);
   }


   /**
    * Método que desplaza el elemento gráfico a unas nuevas coordenadas al
    * tiempo que modifica su ángulo.
    * Para ello tiene en cuenta su posición y velocidad actual. También se puede
    * suministrar un factor para afectar tanto a la velocidad de desplazamiento
    * como a la de giro.
    * Si al desplazar el elemento este queda fuera de la pantalla, se le hace
    * aparecer por el lado opuesto.
    * 
    * @param factor
    *        Factor de multiplicación que permite variar la velocidad de
    *        desplazamiento y la de giro del elemento gráfico.
    */
   public void incrementaPos(double factor)
   {
      // Desplazamos el elemento en el eje X.
      posX += incX * factor;

      // Si salimos de la pantalla, corregimos posición
      if (posX < -ancho / 2)
      {
         posX = view.getWidth() - ancho / 2;
      }

      if (posX > view.getWidth() - ancho / 2)
      {
         posX = -ancho / 2;
      }

      // Desplazamos el elemento en el eje Y
      posY += incY * factor;

      // Si salimos de la pantalla, corregimos posición
      if (posY < -alto / 2)
      {
         posY = view.getHeight() - alto / 2;
      }

      if (posY > view.getHeight() - alto / 2)
      {
         posY = -alto / 2;
      }

      // Giramos el elemento
      angulo += rotacion * factor;
   }


   /**
    * Método que permite obtener la distancia entre dos elementos gráficos (el
    * actual y el que se suministra como parámetro).
    * 
    * @param g
    *        Elemento gráfico desde el que se mide la distancia al actual.
    * @return
    *         La distancia entre el elemento actual y el suministrado.
    */
   public double distancia(Grafico g)
   {
      return Math.hypot(posX - g.posX, posY - g.posY);
   }


   /**
    * Determina si el elemento actual ha colisionado con el que se proporciona
    * como parámetro, según el radio de colisión asignado.
    * 
    * @param g
    *        Elemento gráfico para el que se comprueba la colisión.
    * @return
    *         True si el elemento actual está en colisión con el elemento
    *         gráfico suministrado.
    */
   public boolean verificaColision(Grafico g)
   {
      return (distancia(g) < (radioColision + g.radioColision));
   }


   /**
    * @return el drawable
    */
   public Drawable getDrawable()
   {
      return drawable;
   }


   /**
    * @param drawable
    *        el drawable a establecer
    */
   public void setDrawable(Drawable drawable)
   {
      this.drawable = drawable;
   }


   /**
    * @return el posX
    */
   public double getPosX()
   {
      return posX;
   }


   /**
    * @param posX
    *        el posX a establecer
    */
   public void setPosX(double posX)
   {
      this.posX = posX;
   }


   /**
    * @return el posY
    */
   public double getPosY()
   {
      return posY;
   }


   /**
    * @param posY
    *        el posY a establecer
    */
   public void setPosY(double posY)
   {
      this.posY = posY;
   }


   /**
    * @return el incX
    */
   public double getIncX()
   {
      return incX;
   }


   /**
    * @param incX
    *        el incX a establecer
    */
   public void setIncX(double incX)
   {
      this.incX = incX;
   }


   /**
    * @return el incY
    */
   public double getIncY()
   {
      return incY;
   }


   /**
    * @param incY
    *        el incY a establecer
    */
   public void setIncY(double incY)
   {
      this.incY = incY;
   }


   /**
    * @return el angulo
    */
   public int getAngulo()
   {
      return angulo;
   }


   /**
    * @param angulo
    *        el angulo a establecer
    */
   public void setAngulo(int angulo)
   {
      this.angulo = angulo;
   }


   /**
    * @return el rotacion
    */
   public int getRotacion()
   {
      return rotacion;
   }


   /**
    * @param rotacion
    *        el rotacion a establecer
    */
   public void setRotacion(int rotacion)
   {
      this.rotacion = rotacion;
   }


   /**
    * @return el ancho
    */
   public int getAncho()
   {
      return ancho;
   }


   /**
    * @param ancho
    *        el ancho a establecer
    */
   public void setAncho(int ancho)
   {
      this.ancho = ancho;
   }


   /**
    * @return el alto
    */
   public int getAlto()
   {
      return alto;
   }


   /**
    * @param alto
    *        el alto a establecer
    */
   public void setAlto(int alto)
   {
      this.alto = alto;
   }


   /**
    * @return el radioColision
    */
   public int getRadioColision()
   {
      return radioColision;
   }


   /**
    * @param radioColision
    *        el radioColision a establecer
    */
   public void setRadioColision(int radioColision)
   {
      this.radioColision = radioColision;
   }


   /**
    * @return el view
    */
   public View getView()
   {
      return view;
   }


   /**
    * @param view
    *        el view a establecer
    */
   public void setView(View view)
   {
      this.view = view;
   }


   /**
    * @return el maxVelocidad
    */
   public static int getMaxVelocidad()
   {
      return MAX_VELOCIDAD;
   }
}
