package com.ajvico.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;


class Grafico
{
   private Drawable drawable; // Imagen que dibujaremos

   private double posX, posY; // Posición

   private double incX, incY; // Velocidad desplazamiento

   private int angulo, rotacion;// Ángulo y velocidad rotación

   private int ancho, alto; // Dimensiones de la imagen

   private int radioColision; // Para determinar colisión

   // Donde dibujamos el gráfico (usada en view.invalidate)
   private View view;

   // Para determinar el espacio a borrar (view.invalidate)
   public static final int MAX_VELOCIDAD = 20;


   public Grafico(
      View view,
      Drawable drawable)
   {
      this.view = view;
      this.drawable = drawable;
      ancho = drawable.getIntrinsicWidth();
      alto = drawable.getIntrinsicHeight();
      radioColision = (alto + ancho) / 4;
   }


   public void dibujaGrafico(Canvas canvas)
   {
      canvas.save();

      int x = (int) (posX + ancho / 2);
      int y = (int) (posY + alto / 2);
      canvas.rotate((float) angulo, (float) x, (float) y);

      drawable.setBounds(
         (int) posX,
         (int) posY,
         (int) posX + ancho,
         (int) posY + alto);
      drawable.draw(canvas);

      canvas.restore();

      int rInval = (int) Math.hypot(ancho, alto) / 2 + MAX_VELOCIDAD;
      view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);
   }


   public void incrementaPos(double factor)
   {
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

      posY += incY * factor;

      if (posY < -alto / 2)
      {
         posY = view.getHeight() - alto / 2;
      }

      if (posY > view.getHeight() - alto / 2)
      {
         posY = -alto / 2;
      }

      angulo += rotacion * factor; // Actualizamos ángulo
   }


   public double distancia(Grafico g)
   {
      return Math.hypot(posX - g.posX, posY - g.posY);
   }


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
