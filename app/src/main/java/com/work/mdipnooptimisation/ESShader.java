package com.work.mdipnooptimisation;

import static android.opengl.GLES30.GL_COMPRESSED_RGBA8_ETC2_EAC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.Stream;

import android.content.Context;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.util.Log;

public class ESShader
{


   static private int   COMPRESSED_RGBA_ASTC_10x8_KHR   =        0x93BA;
   static private int   COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR  =  0x93DA;


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








//   public static int[] loadAstcFromAssets(Context context, String nameAstcFile) throws IOException {
//       int returnID = 0;
//
//        InputStream is =  context.getAssets().open(nameAstcFile);
//
//       int[] textureIds = new int[1];
//
//
//       ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//         int nRead;
//         byte[] myB = new byte[16384];
//
//         while ((nRead = is.read(myB, 0, myB.length)) != -1) {
//           buffer.write(myB, 0, nRead);
//           Log.d("df"," in read ! ");
//         }
//
//
//      // byte[] myB = IOUtils.toByteArray(is);
//
//
//       // ETC1Util.ETC1Texture sdfETC =  ETC1Util.createTexture(is);
//
//        //ETC1
//
//        //ESGLTextureUtility.loadFile
//    //InputStream is = ESGLTextureUtility.loadFile(iFile, assetManager);
//      //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//         //byte[] myB =  is.readAllBytes();
//         byte[] arr = buffer.toByteArray();
//             ByteBuffer byteBuff = ByteBuffer.wrap(buffer.toByteArray());
//
//             DataPKM pkmMain = new DataPKM(arr);
//
//             int lWidth = pkmMain.getWidth();
//             int lHeight = pkmMain.getHeight();
//             int mySize = pkmMain.getSize(GL_COMPRESSED_RGBA8_ETC2_EAC);
//
//           // byteBuff.get(arr)
//
//             Log.d("df", " in a this ! ");
//
//
//    GLES20.glGenTextures(1, textureIds, 0);
//    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
//
//    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
//    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//    Log.d("df", " my size == " +buffer.toByteArray().length);
//    callErrorCheck("11");
//
//
//
////    IntBuffer as2 = IntBuffer.allocate(1);
////    int[] dsfsd = new int[101];
////
////    GLES20.glGetIntegerv(GLES20.GL_NUM_COMPRESSED_TEXTURE_FORMATS, as2);  // was GL_MAX_TEXTURE_SIZE ok
////
////       // number texture formats 62
////    Log.d("df", " my number format == " + as2.get());
////
////
////    // GL_COMPRESSED_TEXTURE_FORMATS
////      IntBuffer as3 = IntBuffer.allocate(62);
////      GLES20.glGetIntegerv(GLES20.GL_COMPRESSED_TEXTURE_FORMATS, as3);
////    for(int i = 0; i< 62; i++ ){
////        Log.d("sdf", " my nubmer string = " + as3.get(i));
////        Log.d(" na", " names is = " + GLES20.glGetString(as3.get(i)));
////    }
//
//    Log.d("df", " my ext = " + GLES20.glGetString(GLES20.GL_EXTENSIONS));
//                      //         callErrorCheck("12");0x93BA  0x93BA  GL_COMPRESSED_RGBA8_ETC2_EAC
//
//       // imageSize
//
//       GLES20.glCompressedTexImage2D(GLES20.GL_TEXTURE_2D, 0, GL_COMPRESSED_RGBA8_ETC2_EAC, lWidth, lHeight, 0, mySize, byteBuff);
//
//
//    callErrorCheck("12");
//
////        GL_OES_EGL_image
////        GL_OES_EGL_image_external
////        GL_OES_EGL_sync
////        GL_OES_vertex_half_float
////        GL_OES_framebuffer_object
////        GL_OES_rgb8_rgba8
////        GL_OES_compressed_ETC1_RGB8_texture
////        GL_AMD_compressed_ATC_texture
////        GL_KHR_texture_compression_astc_ldr
////        GL_KHR_texture_compression_astc_hdr
////        GL_OES_texture_compression_astc
////        GL_OES_texture_npot
////        GL_EXT_texture_filter_anisotropic
////        GL_EXT_texture_format_BGRA8888
////        GL_EXT_read_format_bgra GL_OES_texture_3D GL_EXT_color_buffer_float GL_EXT_color_buffer_half_float GL_QCOM_alpha_test GL_OES_depth24 GL_OES_packed_depth_stencil GL_OES_depth_texture GL_OES_depth_texture_cube_map GL_EXT_sRGB GL_OES_texture_float GL_OES_texture_float_linear GL_OES_texture_half_float GL_OES_texture_half_float_linear GL_EXT_texture_type_2_10_10_10_REV GL_EXT_texture_sRGB_decode GL_EXT_texture_format_sRGB_override GL_OES_element_index_uint GL_EXT_copy_image GL_EXT_geometry_shader GL_EXT_tessellation_shader GL_OES_texture_stencil8 GL_EXT_shader_io_blocks GL_OES_shader_image_atomic GL_OES_sample_variables GL_EXT_texture_border_clamp GL_EXT_EGL_image_external_wrap_modes GL_EXT_multisampled_render_to_texture GL_EXT_multisampled_render_to_texture2 GL_OES_shader_multisample_interpolation GL_EXT_texture_cube_map_array GL_EXT_draw_buffers_indexed GL_EXT_gpu_shader5 GL_EXT_robustness GL_EXT_texture_buffer GL_EXT_shader_framebuffer_fetch GL_ARM_shader_framebuffer_fetch_depth_stencil GL_OES_texture_storage_multisample_2d_array GL_OES_sample_shading GL_OES_get_program_binary GL_EXT_debug_label GL_KHR_blend_equation_advanced GL_KHR_blend_equation_advanced_coherent GL_QCOM_tiled_rendering GL_ANDROID_extension_pack_es31a GL_EXT_primitive_bounding_box GL_OES_standard_derivatives GL_OES_vertex_array_object GL_EXT_disjoint_timer_query GL_KHR_debug GL_EXT_YUV_target GL_EXT_sRGB_write_control GL_EXT_texture_norm16 GL_EXT_discard_framebuffer GL_OES_surfaceless_context GL_OVR_multiview GL_OVR_multiview2 GL_EXT_texture_sRGB_R8 GL_KHR_no_error GL_EXT_debug_marker GL_OES_EGL_image_external_essl3 GL_OVR_multiview_multisampled_render_to_texture GL_EXT_buffer_storage GL_EXT_external_buffer GL_EXT_blit_framebuffer_params GL_EXT_clip_cull_distance GL_EXT_protected_textures GL_EXT_shader_non_constant_global_initializers GL_QCOM_texture_foveated GL_QCOM_texture_foveated_subsampled_layout GL_QCOM_shader_framebuffer_fetch_noncoherent GL_QCOM_shader_framebuffer_fetch_rate GL_EXT_memory_object GL_EXT_memory_object_fd GL_EXT_EGL_image_array GL_NV_shader_noperspective_interpolation GL_KHR_robust_buffer_access_behavior GL_EXT_EGL_image_storage GL_EXT_blend_func_extended GL_EXT_clip_control GL_OES_texture_view GL_EXT_fragment_invocation_density GL_QCOM_validate_shader_binary GL_QCOM_YUV_texture_gather
//
//
//
//      //}
//
//    //textureInfo.source = iFile;
//    is.close();
//
//    //this.checkError(gl);
//
//
//
//       return textureIds;
//
//   }


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
