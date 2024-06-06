package com.work.mdipnooptimisation;

import android.app.Activity;
import android.os.Bundle;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class MainActivity extends Activity {


    private final int CONTEXT_CLIENT_VERSION = 2;

   @Override
   protected void onCreate ( Bundle savedInstanceState )
   {
      super.onCreate ( savedInstanceState );
      mGLSurfaceView = new GLSurfaceView ( this );

      if ( detectOpenGLES20() )
      {
         mGLSurfaceView.setEGLContextClientVersion ( CONTEXT_CLIENT_VERSION );
         mGLSurfaceView.setRenderer ( new SimpleVertexShaderRenderer ( this ) );
      }
      else
      {
         Log.e ( "HelloTriangle", "OpenGL ES 3.0 not supported on device.  Exiting..." );
         finish();

      }

      setContentView ( mGLSurfaceView );
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
