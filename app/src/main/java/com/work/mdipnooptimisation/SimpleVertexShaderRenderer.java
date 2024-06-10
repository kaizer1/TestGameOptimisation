package com.work.mdipnooptimisation;

import static android.opengl.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glGetString;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;


public class SimpleVertexShaderRenderer implements GLSurfaceView.Renderer
{


   int[] textureIds = new int[1];
   int[] textureIdsAnother = new int[1];
   private Context mainContext;
   ///
   // Constructor
   //
   private boolean drawCube = true;

   public SimpleVertexShaderRenderer ( Context context )
   {
       mainContext = context;
   }

   public void onSurfaceCreated ( GL10 glUnused, EGLConfig config )
   {


      Log.d("df"," my Shader = " + glGetString(GL_SHADING_LANGUAGE_VERSION));


      String vShaderStr =
         "#version 300 es 							 \n" +
         "uniform mat4 u_mvpMatrix;                   \n" +
         "layout(location = 0) in vec4 a_position;    \n" +
         "layout(location = 1) in vec2 a_color;       \n" +
         "out vec2 v_color;                           \n" +
         "void main()                                 \n" +
         "{                                           \n" +
         "   v_color = a_color;                       \n" +
         "   gl_Position = u_mvpMatrix * a_position;  \n" +
         "}                                           \n";

      String fShaderStr =
         "#version 300 es 							 \n" +
         "precision mediump float;                    \n" +
         "in vec2 v_color;                            \n" +
         "layout(location = 0) out vec4 outColor;     \n" +
         "uniform sampler2D sampL;                    \n" +
         "void main()                                 \n" +
         "{                                           \n" +
         "  ///outColor = v_color;                    \n" +
         "  outColor = texture(sampL, v_color); \n" +
         "}                                           \n";

      mProgramObject = ESShader.loadProgram ( vShaderStr, fShaderStr );

      mMVPLoc = GLES20.glGetUniformLocation ( mProgramObject, "u_mvpMatrix" );

      samplerOur = GLES20.glGetUniformLocation(mProgramObject, "sampL");



        mCube.genCube ( 1.0f ); // mCube.getSphere()
       //mCube.genSphere(3, 3);

        pyramid.getPyramid();

      mAngle = 45.0f;

      // load texture

      final BitmapFactory.Options options = new BitmapFactory.Options();
      options.inScaled = false;

      final Bitmap bitmap = BitmapFactory.decodeResource(
          mainContext.getResources(), R.drawable.dwall, options);

      GLES20.glActiveTexture(GL_TEXTURE0);



       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
      GLES20.glEnable(GLES20.GL_BLEND);


      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);


      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
      bitmap.recycle();




      GLES20.glBindTexture(GL_TEXTURE_2D, 0);


        GLES20.glActiveTexture(GL_TEXTURE1);

                   final Bitmap bitmap2 = BitmapFactory.decodeResource(
          mainContext.getResources(), R.drawable.sphere_texture, options);



       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIdsAnother[0]);

      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
      GLES20.glEnable(GLES20.GL_BLEND);


      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);


      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap2, 0);
      bitmap2.recycle();

      GLES20.glBindTexture(GL_TEXTURE_2D, 0);


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
      if(!drawCube){
  modelview.rotate ( mAngle, 0.0f, 1.0f, 1.0f );
      }else{
          modelview.rotate ( mAngle, 1.0f, 1.0f, 0.0f );
      }

      mMVPMatrix.matrixMultiply ( modelview.get(), perspective.get() );
   }



   public void CallCube(){

      drawCube =true;
   }


   public void CallPyramid(){

      drawCube =false;
   }

   public void onDrawFrame ( GL10 glUnused )
   {
      update();



        	GLES20.glEnable(GLES20.GL_DEPTH_TEST);

      GLES20.glViewport ( 0, 0, mWidth, mHeight );

      GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

      if(drawCube) {


         //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
         GLES20.glUseProgram(mProgramObject);

         GLES20.glActiveTexture(GL_TEXTURE0);
         GLES20.glBindTexture(GL_TEXTURE_2D, textureIds[0]);
GLES20.glUniform1i(samplerOur, 0);

         // half_float  GLES20.GL_UNSIGNED_SHORT
         GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false,
                 0, mCube.getVertices());
         GLES20.glEnableVertexAttribArray(0);

         GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false,
                 0, mCube.getTexCoords());

         GLES20.glEnableVertexAttribArray(1);
         // GLES20.glVertexAttrib4f ( 1, 1.0f, 0.0f, 0.0f, 1.0f );

         GLES20.glUniformMatrix4fv(mMVPLoc, 1, false,
                 mMVPMatrix.getAsFloatBuffer());


         GLES20.glDrawElements(GLES20.GL_TRIANGLES, mCube.getNumIndices(),
                 GLES20.GL_UNSIGNED_SHORT, mCube.getIndices());

      }else {

         GLES20.glUseProgram(mProgramObject);

         GLES20.glActiveTexture(GL_TEXTURE1);
         GLES20.glBindTexture(GL_TEXTURE_2D, textureIdsAnother[0]);

         GLES20.glUniform1i(samplerOur, 1);

         // half_float  GLES20.GL_UNSIGNED_SHORT
         GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false,
                 0, pyramid.getmVerticesPyramid());
         GLES20.glEnableVertexAttribArray(0);

         GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false,
                 0, pyramid.getmTexCoordsPyramid());

         GLES20.glEnableVertexAttribArray(1);
         // GLES20.glVertexAttrib4f ( 1, 1.0f, 0.0f, 0.0f, 1.0f );

         GLES20.glUniformMatrix4fv(mMVPLoc, 1, false,
                 mMVPMatrix.getAsFloatBuffer());

         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);

      }



   }


   public void onSurfaceChanged ( GL10 glUnused, int width, int height )
   {
      mWidth = width;
      mHeight = height;
   }

   private int mProgramObject;

   private int mMVPLoc;
   private int samplerOur;
   private ESShapes mCube = new ESShapes();
   private float mAngle;
   private ESShapes pyramid = new ESShapes();
   private ESTransform mMVPMatrix = new ESTransform();
   private int mWidth;
   private int mHeight;
   private long mLastTime = 0;
}

