package com.work.mdipnooptimisation;

import static android.opengl.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static android.opengl.GLES20.glGetString;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;


public class SimpleVertexShaderRenderer implements GLSurfaceView.Renderer
{

   ///
   // Constructor
   //
   public SimpleVertexShaderRenderer ( Context context )
   {

   }

   public void onSurfaceCreated ( GL10 glUnused, EGLConfig config )
   {


      Log.d("df"," my Shader = " + glGetString(GL_SHADING_LANGUAGE_VERSION));


      String vShaderStr =
         "#version 300 es 							 \n" +
         "uniform mat4 u_mvpMatrix;                   \n" +
         "layout(location = 0) in vec4 a_position;    \n" +
         "layout(location = 1) in vec4 a_color;       \n" +
         "out vec4 v_color;                           \n" +
         "void main()                                 \n" +
         "{                                           \n" +
         "   v_color = a_color;                       \n" +
         "   gl_Position = u_mvpMatrix * a_position;  \n" +
         "}                                           \n";

      String fShaderStr =
         "#version 300 es 							 \n" +
         "precision mediump float;                    \n" +
         "in vec4 v_color;                            \n" +
         "layout(location = 0) out vec4 outColor;     \n" +
         "void main()                                 \n" +
         "{                                           \n" +
         "  outColor = v_color;                       \n" +
         "}                                           \n";

      mProgramObject = ESShader.loadProgram ( vShaderStr, fShaderStr );

      mMVPLoc = GLES20.glGetUniformLocation ( mProgramObject, "u_mvpMatrix" );

      mCube.genCube ( 1.0f );

      mAngle = 45.0f;

      GLES20.glClearColor ( 1.0f, 1.0f, 1.0f, 0.0f );
   }

   private void update()
   {
      if ( mLastTime == 0 )
      {
         mLastTime = SystemClock.uptimeMillis();
      }

      long curTime = SystemClock.uptimeMillis();
      long elapsedTime = curTime - mLastTime;
      float deltaTime = elapsedTime / 1000.0f;
      mLastTime = curTime;

      ESTransform perspective = new ESTransform();
      ESTransform modelview = new ESTransform();
      float aspect;

      mAngle += ( deltaTime * 40.0f );

      if ( mAngle >= 360.0f )
      {
         mAngle -= 360.0f;
      }

      aspect = ( float ) mWidth / ( float ) mHeight;

      perspective.matrixLoadIdentity();
      perspective.perspective ( 60.0f, aspect, 1.0f, 20.0f );

      modelview.matrixLoadIdentity();
      modelview.translate ( 0.0f, 0.0f, -2.0f );
      modelview.rotate ( mAngle, 1.0f, 0.0f, 1.0f );
      mMVPMatrix.matrixMultiply ( modelview.get(), perspective.get() );
   }


   public void onDrawFrame ( GL10 glUnused )
   {
      update();

      GLES20.glViewport ( 0, 0, mWidth, mHeight );

      GLES20.glClear ( GLES20.GL_COLOR_BUFFER_BIT );

      GLES20.glUseProgram ( mProgramObject );

     // half_float  GLES20.GL_UNSIGNED_SHORT
      GLES20.glVertexAttribPointer ( 0, 3, GLES20.GL_FLOAT, false,
                                     0, mCube.getVertices() );
      GLES20.glEnableVertexAttribArray ( 0 );

      GLES20.glVertexAttrib4f ( 1, 1.0f, 0.0f, 0.0f, 1.0f );

      GLES20.glUniformMatrix4fv ( mMVPLoc, 1, false,
                                  mMVPMatrix.getAsFloatBuffer() );

      GLES20.glDrawElements ( GLES20.GL_TRIANGLES, mCube.getNumIndices(),
                              GLES20.GL_UNSIGNED_SHORT, mCube.getIndices() );
   }


   public void onSurfaceChanged ( GL10 glUnused, int width, int height )
   {
      mWidth = width;
      mHeight = height;
   }

   private int mProgramObject;

   private int mMVPLoc;
   private ESShapes mCube = new ESShapes();
   private float mAngle;
   private ESTransform mMVPMatrix = new ESTransform();
   private int mWidth;
   private int mHeight;
   private long mLastTime = 0;
}

