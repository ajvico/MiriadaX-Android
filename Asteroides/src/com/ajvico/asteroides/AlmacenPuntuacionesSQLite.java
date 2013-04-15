/**
 * 
 */
package com.ajvico.asteroides;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AlmacenPuntuacionesSQLite
   extends SQLiteOpenHelper
   implements IAlmacenPuntuaciones
{
   public AlmacenPuntuacionesSQLite(
      Context context)
   {
      // Llamamos al constructor de SQLiteOpenHelper, indicando el nombre de la
      // base de datos y la versión9
      super(context, "puntuaciones", null, 1);
   }


   /* Métodos de SQLiteOpenHelper */

   /**
    * Se ejecuta cuando se crea la base de datos por primera vez.
    * 
    * @param db
    *        Base de datos que se ha creado.
    * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
    */
   @Override
   public void onCreate(SQLiteDatabase db)
   {
      // Añadimos una tabla para las puntuaciones a la base de datos
      db.execSQL("CREATE TABLE puntuaciones ("
         + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
         + "puntos INTEGER, nombre TEXT, fecha LONG)");
   }


   /**
    * Se ejecuta si la versión actual de la base de datos es inferior a la
    * indicada en el constructor de SQLiteOpenHelper.
    * 
    * @param db
    *        Base de datos actual.
    * @param oldVersion
    *        Versión actual de la base de datos.
    * @param newVersion
    *        Versión a la que hay que convertir la base de datos (la del
    *        constructor de SQLiteOpenHelper).
    * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
    *      int, int)
    */
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
   {
      // En caso de una nueva versión habría que actualizar las tablas
   }


   /* Métodos de AlmacenPuntuaciones */

   public void guardarPuntuacion(int puntos, String nombre, long fecha)
   {
      // Obtenemos acceso a la base de datos en modo de escritura
      // La base de datos se creará si no existe
      SQLiteDatabase db = getWritableDatabase();

      // Insertamos la puntuación en la tabla de las puntuaciones
      db.execSQL(
         "INSERT INTO puntuaciones VALUES ( null, "
            + puntos + ", '" + nombre + "', " + fecha + ")");
   }


   public Vector<String> listaPuntuaciones(int cantidad)
   {
      Vector<String> result = new Vector<String>();

      // Obtenemos acceso a la base de datos en modo de lectura
      // La base de datos se creará si no existe
      SQLiteDatabase db = getReadableDatabase();

      // Creamos un cursor para leer las puntuaciones
      // Cursor cursor =
      // db.rawQuery(
      // "SELECT puntos, nombre FROM puntuaciones ORDER BY puntos DESC LIMIT "
      // + cantidad,
      // null);
      String[] CAMPOS = { "puntos", "nombre" };
      Cursor cursor =
         db.query(
            "puntuaciones",
            CAMPOS,
            null,
            null,
            null,
            null,
            "puntos DESC",
            Integer.toString(cantidad));

      // Recorremos el cursor
      while (cursor.moveToNext())
      {
         // Obtenemos los datos de cada puntuación y los agregamos al vector
         result.add(cursor.getInt(0) + " " + cursor.getString(1));
      }

      // Cerramos el cursor
      cursor.close();

      // Devolvemos las puntuaciones obtenidas
      return result;
   }
}
