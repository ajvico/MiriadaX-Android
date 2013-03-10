package com.ajvico.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;


/**
 * Clase privada para gestionar los elementos gr�ficos del juego.
 */
class Grafico
{
   /**
    * Imagen que dibujaremos.
    */
   private Drawable drawable;

   /**
    * Coordenada X del elemento gr�fico.
    */
   private double posX;

   /**
    * Coordenada Y del elemento gr�fico.
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
    * �nglulo de rotaci�n.
    */
   private int angulo;

   /**
    * Velocidad de rotaci�n.
    */
   private int rotacion;

   /**
    * Ancho de la imagen del elemento gr�fico.
    */
   private int ancho;

   /**
    * Alto de la imagen del elemento gr�fico.
    */
   private int alto;

   /**
    * Radio de la circunferencia en la que se circunscribe el elemento, seg�n
    * sus dimensiones.
    */
   private int radio_elemento;

   /**
    * Para determinar la colisi�n de un elemento gr�fico con otro.
    */
   private int radioColision;

   /**
    * D�nde dibujamos el gr�fico (usada en view.invalidate).
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
    *        Vista sobre la que dibujaremos el elemento gr�fico.
    * @param drawable
    *        Imagen que hay que dibujar.
    */
   public Grafico(
      View view,
      Drawable drawable)
   {
      // Guardamos los par�metros recibidos en la clase
      this.view = view;
      this.drawable = drawable;

      // Obtenemos las dimensiones de la imagen a dibujar
      ancho = drawable.getIntrinsicWidth();
      alto = drawable.getIntrinsicHeight();

      // Calculamos el radio de colisi�n seg�n el tama�o de la imagen
      radioColision = (alto + ancho) / 4;

      // Calculamos el radio de la circunferencia en la que se circunscribe el
      // elemento
      radio_elemento = (int) Math.hypot(ancho, alto) / 2;
   }


   /**
    * M�todo que se encarga de pintar el elemento gr�fico en el lienzo
    * suministrado.
    * 
    * @param canvas
    *        Lienzo en el que se pintar� el elemento gr�fico.
    */
   public void dibujaGrafico(Canvas canvas)
   {
      // Guardamos la matriz y el �rea de recorte actuales
      canvas.save();

      // Obtenemos las coordenadas actuales del centro del elemento
      int x = (int) (posX + ancho / 2);
      int y = (int) (posY + alto / 2);

      // Rotamos el elemento sobre su centro, seg�n el �ngulo actual
      canvas.rotate((float) angulo, (float) x, (float) y);

      // Definimos el �rea d�nde se dibujar� el elemento gr�fico, seg�n sus
      // coordenadas actuales y sus dimensiones
      drawable.setBounds(
         (int) posX,
         (int) posY,
         (int) posX + ancho,
         (int) posY + alto);

      // Dibujamos el elemento gr�fico en el lienzo proporcionado
      drawable.draw(canvas);

      // Restauramos la matriz y el �rea de recorte del lienzo para que los
      // ajustes realizados en este elemento gr�fico no afecten al resto de
      // cosas que se pinten en dicho lienzo
      canvas.restore();

      // // Calculamos el �rea m�xima que puede ocupar el elemento gr�fico al
      // // desplazarse
      // int rInval = (int) Math.hypot(ancho, alto) / 2 + MAX_VELOCIDAD;
      //
      // // Forzamos a que se vuelva a dibujar el �rea m�xima que se ha podido
      // ver
      // // afectada por el movimiento que ha realizado el elemento gr�fico
      // view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);
   }


   /**
    * M�todo que desplaza el elemento gr�fico a unas nuevas coordenadas al
    * tiempo que modifica su �ngulo.
    * Para ello tiene en cuenta su posici�n y velocidad actual. Tambi�n se puede
    * suministrar un factor para afectar tanto a la velocidad de desplazamiento
    * como a la de giro.
    * Si al desplazar el elemento este queda fuera de la pantalla, se le hace
    * aparecer por el lado opuesto.
    * 
    * @param factor
    *        Factor de multiplicaci�n que permite variar la velocidad de
    *        desplazamiento y la de giro del elemento gr�fico.
    */
   public void incrementaPos(double factor)
   {
      boolean fuera_pantalla = false;
      double posX_ini = posX;
      double posY_ini = posY;

      // Desplazamos el elemento en el eje X.
      posX += incX * factor;

      // Si salimos de la pantalla, corregimos posici�n
      if (posX < -ancho / 2)
      {
         posX = view.getWidth() - ancho / 2;
         fuera_pantalla = true;
      }

      if (posX > view.getWidth() - ancho / 2)
      {
         posX = -ancho / 2;
         fuera_pantalla = true;
      }

      // Desplazamos el elemento en el eje Y
      posY += incY * factor;

      // Si salimos de la pantalla, corregimos posici�n
      if (posY < -alto / 2)
      {
         posY = view.getHeight() - alto / 2;
         fuera_pantalla = true;
      }

      if (posY > view.getHeight() - alto / 2)
      {
         posY = -alto / 2;
         fuera_pantalla = true;
      }

      // Giramos el elemento
      angulo += rotacion * factor;

      // Si nos hemos salido de la pantalla hay que invalidar tambi�n la zona
      // que ocupaba el elemento antes, para que se actualice correctamente
      if (fuera_pantalla)
      {
         // Obtenemos las coordenadas anteriores del centro del elemento
         int x_ini = (int) (posX_ini + ancho / 2);
         int y_ini = (int) (posY_ini + alto / 2);

         // Forzamos a que se vuelva a dibujar el �rea que ocupaba el elemento
         // antes de salir por el borde de la pantalla
         view.postInvalidate(
            x_ini - radio_elemento,
            y_ini - radio_elemento,
            x_ini + radio_elemento,
            y_ini + radio_elemento);
      }

      // Obtenemos las coordenadas actuales del centro del elemento
      int x = (int) (posX + ancho / 2);
      int y = (int) (posY + alto / 2);

      // Obtenemos el area afectada por el elemento, teniendo en cuenta su
      // desplazamiento
      int rInval = radio_elemento + MAX_VELOCIDAD;

      // Forzamos a que se vuelva a dibujar el �rea m�xima que se ha podido ver
      // afectada por el movimiento que ha realizado el elemento gr�fico
      view.postInvalidate(x - rInval, y - rInval, x + rInval, y + rInval);
   }


   /**
    * Fuerza a que se vuelva a dibujar el �rea que ocupa actualmente el elemento
    * gr�fico en la vista.
    */
   public void invalidar()
   {
      // Obtenemos las coordenadas actuales del centro del elemento
      int x = (int) (posX + ancho / 2);
      int y = (int) (posY + alto / 2);

      // Forzamos a que se vuelva a dibujar el �rea que ocupa actualmente el
      // elemento
      view.postInvalidate(
         x - radio_elemento,
         y - radio_elemento,
         x + radio_elemento,
         y + radio_elemento);
   }


   /**
    * M�todo que permite obtener la distancia entre dos elementos gr�ficos (el
    * actual y el que se suministra como par�metro).
    * 
    * @param g
    *        Elemento gr�fico desde el que se mide la distancia al actual.
    * @return
    *         La distancia entre el elemento actual y el suministrado.
    */
   public double distancia(Grafico g)
   {
      return Math.hypot(posX - g.posX, posY - g.posY);
   }


   /**
    * Determina si el elemento actual ha colisionado con el que se proporciona
    * como par�metro, seg�n el radio de colisi�n asignado.
    * 
    * @param g
    *        Elemento gr�fico para el que se comprueba la colisi�n.
    * @return
    *         True si el elemento actual est� en colisi�n con el elemento
    *         gr�fico suministrado.
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
