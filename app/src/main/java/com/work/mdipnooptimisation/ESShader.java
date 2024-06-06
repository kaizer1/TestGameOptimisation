package com.work.mdipnooptimisation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class ESShader
{

   private static String readShader ( Context context, String fileName )
   {
      String shaderSource = null;

      if ( fileName == null )
      {
         return shaderSource;
      }

      // Read the shader file from assets
      InputStream is = null;
      byte [] buffer;

      try
      {
         is =  context.getAssets().open ( fileName );

         // Create a buffer that has the same size as the InputStream
         buffer = new byte[is.available()];

         // Read the text file as a stream, into the buffer
         is.read ( buffer );

         ByteArrayOutputStream os = new ByteArrayOutputStream();

         // Write this buffer to the output stream
         os.write ( buffer );

         // Close input and output streams
         os.close();
         is.close();

         shaderSource = os.toString();
      }
      catch ( IOException ioe )
      {
         is = null;
      }

      if ( is == null )
      {
         return shaderSource;
      }

      return shaderSource;
   }

   public static int loadShader ( int type, String shaderSrc )
   {
      int shader;
      int[] compiled = new int[1];

      // Create the shader object
      shader = GLES20.glCreateShader ( type );

      if ( shader == 0 )
      {
         return 0;
      }


      GLES20.glShaderSource ( shader, shaderSrc );
      GLES20.glCompileShader ( shader );
      GLES20.glGetShaderiv ( shader, GLES20.GL_COMPILE_STATUS, compiled, 0 );

      if ( compiled[0] == 0 )
      {
         Log.e ( "ESShader", GLES20.glGetShaderInfoLog ( shader ) );
         GLES20.glDeleteShader ( shader );
         return 0;
      }

      return shader;
   }

   public static int loadProgram ( String vertShaderSrc, String fragShaderSrc )
   {
      int vertexShader;
      int fragmentShader;
      int programObject;
      int[] linked = new int[1];

      Log.d(" sta", " start load shader vertext 1 ");
      vertexShader = loadShader ( GLES20.GL_VERTEX_SHADER, vertShaderSrc );

      if ( vertexShader == 0 )
      {
         return 0;
      }

        Log.d(" sta", " start load shader fragment  2 ");
      fragmentShader = loadShader ( GLES20.GL_FRAGMENT_SHADER, fragShaderSrc );

      if ( fragmentShader == 0 )
      {
         GLES20.glDeleteShader ( vertexShader );
         return 0;
      }

      // Create the program object
      programObject = GLES20.glCreateProgram();

      if ( programObject == 0 )
      {
         return 0;
      }

      GLES20.glAttachShader ( programObject, vertexShader );
      GLES20.glAttachShader ( programObject, fragmentShader );

      // Link the program
      GLES20.glLinkProgram ( programObject );

      // Check the link status
      GLES20.glGetProgramiv ( programObject, GLES20.GL_LINK_STATUS, linked, 0 );

      if ( linked[0] == 0 )
      {
         Log.e ( "ESShader", "Error linking program:" );
         Log.e ( "ESShader", GLES20.glGetProgramInfoLog ( programObject ) );
         GLES20.glDeleteProgram ( programObject );
         return 0;
      }

      GLES20.glDeleteShader ( vertexShader );
      GLES20.glDeleteShader ( fragmentShader );

      return programObject;
   }

   public static int loadProgramFromAsset ( Context context, String vertexShaderFileName, String fragShaderFileName )
   {
      int vertexShader;
      int fragmentShader;
      int programObject;
      int[] linked = new int[1];

      String vertShaderSrc = null;
      String fragShaderSrc = null;

      vertShaderSrc = readShader ( context, vertexShaderFileName );

      if ( vertShaderSrc == null )
      {
         return 0;
      }



      fragShaderSrc = readShader ( context, fragShaderFileName );

      if ( fragShaderSrc == null )
      {
         return 0;
      }



      vertexShader = loadShader ( GLES20.GL_VERTEX_SHADER, vertShaderSrc );

      if ( vertexShader == 0 )
      {
         return 0;
      }


      fragmentShader = loadShader ( GLES20.GL_FRAGMENT_SHADER, fragShaderSrc );

      if ( fragmentShader == 0 )
      {
         GLES20.glDeleteShader ( vertexShader );
         return 0;
      }



      programObject = GLES20.glCreateProgram();

      if ( programObject == 0 )
      {
         return 0;
      }

      GLES20.glAttachShader ( programObject, vertexShader );
      GLES20.glAttachShader ( programObject, fragmentShader );

      GLES20.glLinkProgram ( programObject );
      GLES20.glGetProgramiv ( programObject, GLES20.GL_LINK_STATUS, linked, 0 );

      if ( linked[0] == 0 )
      {
         Log.e ( "ESShader", "Error linking program:" );
         Log.e ( "ESShader", GLES20.glGetProgramInfoLog ( programObject ) );
         GLES20.glDeleteProgram ( programObject );
         return 0;
      }



      GLES20.glDeleteShader ( vertexShader );
      GLES20.glDeleteShader ( fragmentShader );

      return programObject;
   }
}
