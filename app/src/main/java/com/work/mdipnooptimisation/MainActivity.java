package com.work.mdipnooptimisation;

import android.app.Activity;
import android.os.Bundle;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {


    private final int CONTEXT_CLIENT_VERSION = 2;
    private SimpleVertexShaderRenderer render;

   @Override
   protected void onCreate ( Bundle savedInstanceState )
   {
      super.onCreate ( savedInstanceState );

      setContentView(R.layout.activity_main);
      mGLSurfaceView =   findViewById(R.id.surface_view); //new GLSurfaceView ( this );

      render = new SimpleVertexShaderRenderer ( this );

      Button cu = findViewById(R.id.cube);
      Button py = findViewById(R.id.sphere);

      if ( detectOpenGLES20() )
      {
         mGLSurfaceView.setEGLContextClientVersion ( CONTEXT_CLIENT_VERSION );

         mGLSurfaceView.setEGLConfigChooser(8,8,8,8,16,0);
         mGLSurfaceView.setRenderer (  render );
      }
      else
      {
         Log.e ( "HelloTriangle", "OpenGL ES 3.0 not supported on device.  Exiting..." );
         finish();

      }


      cu.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
              render.CallCube();
         }
      });


      py.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            render.CallPyramid();
         }
      });

      //setContentView ( mGLSurfaceView );
   }

   private boolean detectOpenGLES20()
   {
      ActivityManager am =
         ( ActivityManager ) getSystemService ( Context.ACTIVITY_SERVICE );
      ConfigurationInfo info = am.getDeviceConfigurationInfo();
      return ( info.reqGlEsVersion >= 0x20000 );
   }

   @Override
   protected void onResume()
   {
      super.onResume();
      mGLSurfaceView.onResume();
   }

   @Override
   protected void onPause()
   {
      super.onPause();
      mGLSurfaceView.onPause();
   }

   private GLSurfaceView mGLSurfaceView;

}
