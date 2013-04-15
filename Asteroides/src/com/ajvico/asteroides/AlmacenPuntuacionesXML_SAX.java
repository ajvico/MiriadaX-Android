/**
 * 
 */
package com.ajvico.asteroides;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Log;
import android.util.Xml;


public class AlmacenPuntuacionesXML_SAX
   implements IAlmacenPuntuaciones
{
   /**
    * Clase para almacenar los datos obtenidos del archivo XML.
    */
   private class ListaPuntuaciones
   {

      private class Puntuacion
      {
         int puntos;

         String nombre;

         long fecha;
      } // class Puntuacion

      /**
       * Clase para gestionar los eventos que se producen al leer el archivo
       * XML.
       * Aquí se obtienen los datos.
       * 
       * Estructura que se espera en el archivo: <code>
       *    <?xml version="1.0" encoding="UTF-8"?>
       *    <lista_puntuaciones>
       *       <puntuacion fecha="1288122023410">
       *          <nombre>Mi nombre</nombre>
       *          <puntos>45000</puntos>
       *       </puntuacion>
       *       <puntuacion fecha="1288122428132">
       *          <nombre>Otro nombre</nombre>
       *          <puntos>31000</puntos>
       *       </puntuacion>
       *    </lista_puntuaciones>
       * </code>
       */
      class ManejadorXML
         extends DefaultHandler
      {
         /**
          * El contenido de los elementos se obtienen línea a línea.
          * Mientras se completan, se van almacenando aquí.
          */
         private StringBuilder cadena;

         /**
          * Estructura de datos donde se guardará cada elemento principal
          * obtenido del archivo XML.
          */
         private Puntuacion puntuacion;


         /**
          * Se ejecuta cuando se abre el archivo XML.
          * Aquí creamos las estructuras necesarias para almacenar los datos.
          * 
          * @throws SAXException
          * @see org.xml.sax.helpers.DefaultHandler#startDocument()
          */
         @Override
         public void startDocument() throws SAXException
         {
            // Creamos la lista para guardar las puntuaciones obtenidas
            // El atributo está definido en una clase superior.
            listaPuntuaciones = new ArrayList<Puntuacion>();

            // Creamos un contenedor para el contenido del los elementos del
            // archivo XML.
            cadena = new StringBuilder();
         } // startDocument


         /**
          * Se ejecuta cuando comienza un nuevo elemento en el archivo XML.
          * 
          * @param uri
          *        La URI del espacio de nombres en el que se define el
          *        elemento. Puede estar vacía.
          * @param nombreLocal
          *        Nombre del elemento, sin cualificar, en el archivo XML.
          * @param nombreCualif
          *        Nombre del elemento en el archivo XML, incluyendo su prefijo.
          * @param atr
          *        Lista de atributos que se incluyen en el elemento.
          * @throws SAXException
          * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
          *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
          */
         @Override
         public void startElement(
            String uri,
            String nombreLocal,
            String nombreCualif,
            Attributes atr) throws SAXException
         {
            // Borramos lo que haya en el contenedor (ya se habrá procesado)
            cadena.setLength(0);

            // Si lo que empieza es un elemento puntuación
            if (nombreLocal.equals("puntuacion"))
            {
               // Creamos una estructura para guardar los datos de la puntuación
               puntuacion = new Puntuacion();

               // Extraemos la fecha del atributo.
               puntuacion.fecha = Long.parseLong(atr.getValue("fecha"));
            }
         } // startElement


         /**
          * Se ejecuta cada vez que se lee parte del contenido (texto) de un
          * elemento del archivo XML.
          * 
          * @param ch
          *        Buffer que contiene los caracteres obtenidos del archivo XML.
          * @param comienzo
          *        Posición del array donde comienzan los caracteres obtenidos.
          * @param lon
          *        Cantidad de caractéres válidos contenidos en el buffer.
          * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
          */
         @Override
         public void characters(char ch[], int comienzo, int lon)
         {
            // Añadimos los caracteres leídos a nuestro contenedor
            cadena.append(ch, comienzo, lon);
         } // characters


         /**
          * Se ejecuta cuando finaliza un elemento en el archivo XML.
          * 
          * @param uri
          *        La URI del espacio de nombres en el que se define el
          *        elemento. Puede estar vacía.
          * @param nombreLocal
          *        Nombre del elemento, sin cualificar, en el archivo XML.
          * @param nombreCualif
          *        Nombre del elemento en el archivo XML, incluyendo su prefijo.
          * @throws SAXException
          * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
          *      java.lang.String, java.lang.String)
          */
         @Override
         public void endElement(
            String uri,
            String nombreLocal,
            String nombreCualif) throws SAXException
         {
            // Si el elemento que finaliza es <puntos>
            if (nombreLocal.equals("puntos"))
            {
               // Almacenamos los puntos en la estructura de datos
               puntuacion.puntos = Integer.parseInt(cadena.toString());
            }
            // Si el elemento que finaliza es <nombre>
            else if (nombreLocal.equals("nombre"))
            {
               // Almacenamos el nombre del jugador en la estructura de datos
               puntuacion.nombre = cadena.toString();
            }
            // Si el elemento que finaliza es <puntuacion>
            else if (nombreLocal.equals("puntuacion"))
            {
               // Añadimos la estructura de datos a la lista de puntuacuones
               listaPuntuaciones.add(puntuacion);
            }
         } // endElement


         /**
          * Se ejecuta cuando finaliza la lectura del documento.
          * 
          * @throws SAXException
          * @see org.xml.sax.helpers.DefaultHandler#endDocument()
          */
         @Override
         public void endDocument() throws SAXException
         {
         }
      } // class ManejadorXML


      /* Atributos de ListaPuntuaciones */

      private List<Puntuacion> listaPuntuaciones;


      /* Métodos de ListaPuntuaciones */

      /**
       * Constructor de la clase.
       */
      public ListaPuntuaciones()
      {
         // Creamos una lista vacía para el caso inicial, cuando no hay
         // puntuaciones.
         // Así se podrá almacenar la primera de todas.
         listaPuntuaciones = new ArrayList<Puntuacion>();
      } // ListaPuntuaciones


      /**
       * Permite añadir una nueva puntuación a la lista, procedente del juego.
       * 
       * @param puntos
       *        Puntos obtenidos.
       * @param nombre
       *        Nombre del jugador.
       * @param fecha
       *        Fecha en que se han obtenido los puntos.
       */
      public void nuevo(int puntos, String nombre, long fecha)
      {
         // Creamos una estructura para gaurdar los datos
         Puntuacion puntuacion = new Puntuacion();

         // Almacenamos los datos que nos proporcionan en la estructura
         puntuacion.puntos = puntos;
         puntuacion.nombre = nombre;
         puntuacion.fecha = fecha;

         // Almacenamos la estructura de datos en la lista de puntuaciones
         listaPuntuaciones.add(puntuacion);
      } // nuevo


      /**
       * Método que permite convertir la lista de puntuaciones actual en un
       * vector de cadenas, concatenando el nombre del jugador con la puntuación
       * que obtuvo.
       * 
       * @return
       *         Un vector de cadenas con las puntuaciones.
       */
      public Vector<String> aVectorString()
      {
         Vector<String> result = new Vector<String>();
         for (Puntuacion puntuacion : listaPuntuaciones)
         {
            result.add(puntuacion.nombre + " " + puntuacion.puntos);
         }
         return result;
      } // aVectorString


      /**
       * Método que permite leer las puntuaciones del archivo XML.
       * 
       * @param entrada
       *        Archivo XML del que se obtienen las puntuaciones.
       * @throws Exception
       */
      public void leerXML(InputStream entrada) throws Exception
      {
         // Se crea una fábrica de parsers XML
         SAXParserFactory fabrica = SAXParserFactory.newInstance();

         // Se crea un nuevo parser XML
         SAXParser parser = fabrica.newSAXParser();

         // Se obtiene un lector a partir del parser XML
         XMLReader lector = parser.getXMLReader();

         // Se crea un gestor de eventos para la lectura del XML
         ManejadorXML manejadorXML = new ManejadorXML();

         // Se asigna el gestor de eventos al lector XML
         lector.setContentHandler(manejadorXML);

         // Se procesa el archivo XML con el lector y el gestor de eventos
         lector.parse(new InputSource(entrada));

         // Se indica que el archivo XML se ha cargado.
         cargadaLista = true;
      } // leerXML


      /**
       * Método que permite guardar la lista de puntuaciones en un archivo XML.
       * 
       * @param salida
       *        Archivo XML donde se guardarán las puntuaciones.
       */
      public void escribirXML(OutputStream salida)
      {
         // Creamos un serializador para generar el XML
         XmlSerializer serializador = Xml.newSerializer();

         try
         {
            // Asociamos el archivo XML destino al serializador
            serializador.setOutput(salida, "UTF-8");

            // Creamos la cabecera del documento XML, indicando que es un
            // documento independiente (sin esquema)
            serializador.startDocument("UTF-8", true);

            // Creamos la etiqueta raíz del XML
            serializador.startTag("", "lista_puntuaciones");

            // Recorremos la lista de puntuaciones
            for (Puntuacion puntuacion : listaPuntuaciones)
            {
               // Creamos una etiqueta <puntuacion>
               serializador.startTag("", "puntuacion");

               // A la etiqueta <puntuacion> le añadimos la fecha como atributo
               serializador.attribute(
                  "",
                  "fecha",
                  String.valueOf(puntuacion.fecha));

               // Añadimos el nombre del jugador en una etiqueta <nombre>
               serializador.startTag("", "nombre");
               serializador.text(puntuacion.nombre);
               serializador.endTag("", "nombre");

               // Añadimos la puntuación en una etiqueta <puntos>
               serializador.startTag("", "puntos");
               serializador.text(String.valueOf(puntuacion.puntos));
               serializador.endTag("", "puntos");

               // Cerramos la etiqueta <puntuacion>
               serializador.endTag("", "puntuacion");
            }

            // Cerramos la etiqueta raíz del XML
            serializador.endTag("", "lista_puntuaciones");

            // Finalizamos el documento XML
            serializador.endDocument();
         }
         catch (Exception e)
         {
            Log.e("Asteroides", e.getMessage(), e);
         }
      } // escribirXML
   } // class ListaPuntuaciones


   /* Atributos de AlmacenPuntuacionesXML_SAX */

   /**
    * Nombre del ficheor XML donde se almacenarán las puntuaciones.
    */
   private static String FICHERO = "puntuaciones.xml";

   /**
    * Actividad que mostrará las puntuaciones.
    */
   private Context contexto;

   /**
    * Lista de puntuaciones. Almacena el contenido del XML en memoria.
    */
   private ListaPuntuaciones lista;

   /**
    * Flag que indica que el archivo XML ya se ha leído.
    */
   private boolean cargadaLista;


   /* Métodos de AlmacenPuntuacionesXML_SAX */

   public AlmacenPuntuacionesXML_SAX(
      Context contexto)
   {
      this.contexto = contexto;
      lista = new ListaPuntuaciones();
      cargadaLista = false;
   } // AlmacenPuntuacionesXML_SAX


   @Override
   public void guardarPuntuacion(int puntos, String nombre, long fecha)
   {
      try
      {
         // Si no hemos leído aún el archivo XML, lo hacemos ahora
         if (!cargadaLista)
         {
            lista.leerXML(contexto.openFileInput(FICHERO));
         }
      }
      catch (FileNotFoundException e)
      {
      }
      catch (Exception e)
      {
         Log.e("Asteroides", e.getMessage(), e);
      }

      // Añadimos la nueva puntuación a la lista
      lista.nuevo(puntos, nombre, fecha);

      try
      {
         // Guardamos la lista actualizada en el archivo XML
         lista.escribirXML(contexto.openFileOutput(
            FICHERO,
            Context.MODE_PRIVATE));
      }
      catch (Exception e)
      {
         Log.e("Asteroides", e.getMessage(), e);
      }
   } // guardarPuntuacion


   @Override
   public Vector<String> listaPuntuaciones(int cantidad)
   {
      try
      {
         // Si no hemos leído aún el archivo XML, lo hacemos ahora
         if (!cargadaLista)
         {
            // Obtenemos la lista de puntuaciones del archivo XML
            lista.leerXML(contexto.openFileInput(FICHERO));
         }
      }
      catch (Exception e)
      {
         Log.e("Asteroides", e.getMessage(), e);
      }

      // Convertimos la lista de puntuaciones en un vector de cadenas y la
      // devolvemos
      return lista.aVectorString();
   } // listaPuntuaciones
} // class AlmacenPuntuacionesXML_SAX