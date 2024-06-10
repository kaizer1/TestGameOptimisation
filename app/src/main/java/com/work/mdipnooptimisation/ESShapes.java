package com.work.mdipnooptimisation;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ESShapes
{


   public int getPyramid(){


      int arrayCount = 0;


      float[] pyramidVerts = {
                              0.0f, 0.8f, 0.0f,
                              -0.3f, -0.5f, -0.3f,
                              0.3f, -0.5f, -0.3f,

                             0.0f, 0.8f, 0.0f,
                            -0.3f, -0.5f, 0.3f,
                             0.3f, -0.5f, 0.3f,

                            0.0f, 0.8f, 0.0f,
                            0.3f, -0.5f, 0.3f,
                            0.3f, -0.5f, -0.3f,

                             0.0f, 0.8f, 0.0f,
                            -0.3f, -0.5f, 0.3f,
                            -0.3f, -0.5f, -0.3f,

                            -0.3f, -0.5f, 0.3f,
                            -0.3f, -0.5f, -0.3f,
                             0.3f, -0.5f,  0.3f,

                            -0.3f, -0.5f, 0.3f,
                             0.3f, -0.5f, -0.3f,
                             0.3f, -0.5f,  0.3f,

                          };



      //    0, 2, 1, 0, 3, 2,
      float[] pyramidTex = {

              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,   // 2
              // 0.35f, 0.33f,   // 3
              //0.35f, 0.01f,   // 4
              // 0.0f, 0.01f,    // 5
              // 0.3f, 0.0f
              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,
              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,

              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,

              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,

              0.0f, 0.01f,    // 0
              0.0f, 0.33f,   // 1
              0.35f, 0.33f,
      };


       mVerticesPyramid = ByteBuffer.allocateDirect ( 18 * 3 * 4 )
                  .order ( ByteOrder.nativeOrder() ).asFloatBuffer();

      mTexCoordsPyramid = ByteBuffer.allocateDirect ( 18 * 2 * 4 )
                   .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
//      mIndices = ByteBuffer.allocateDirect ( numIndices * 2 )
//                 .order ( ByteOrder.nativeOrder() ).asShortBuffer();

      mVerticesPyramid.put ( pyramidVerts ).position ( 0 );
      mTexCoordsPyramid.put ( pyramidTex ).position ( 0 );



      return arrayCount;
   }


   public int genSphere ( int numSlices, float radius )
   {
      int i;
      int j;
      int numParallels = numSlices;
      int numVertices = ( numParallels + 1 ) * ( numSlices + 1 );
      int numIndices = numParallels * numSlices * 6;
      float angleStep = ( ( 2.0f * ( float ) Math.PI ) / numSlices );

      // Allocate memory for buffers
      mVertices = ByteBuffer.allocateDirect ( numVertices * 3 * 4 )
                  .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mNormals = ByteBuffer.allocateDirect ( numVertices * 3 * 4 )
                 .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mTexCoords = ByteBuffer.allocateDirect ( numVertices * 2 * 4 )
                   .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mIndices = ByteBuffer.allocateDirect ( numIndices * 2 )
                 .order ( ByteOrder.nativeOrder() ).asShortBuffer();

      for ( i = 0; i < numParallels + 1; i++ )
      {
         for ( j = 0; j < numSlices + 1; j++ )
         {
            int vertex = ( i * ( numSlices + 1 ) + j ) * 3;

            mVertices
            .put ( vertex + 0,
                   ( float ) ( radius
                               * Math.sin ( angleStep * ( float ) i ) * Math
                               .sin ( angleStep * ( float ) j ) ) );

            mVertices.put ( vertex + 1,
                            ( float ) ( radius * Math.cos ( angleStep * ( float ) i ) ) );
            mVertices
            .put ( vertex + 2,
                   ( float ) ( radius
                               * Math.sin ( angleStep * ( float ) i ) * Math
                               .cos ( angleStep * ( float ) j ) ) );

            mNormals.put ( vertex + 0, mVertices.get ( vertex + 0 ) / radius );
            mNormals.put ( vertex + 1, mVertices.get ( vertex + 1 ) / radius );
            mNormals.put ( vertex + 2, mVertices.get ( vertex + 2 ) / radius );

            int texIndex = ( i * ( numSlices + 1 ) + j ) * 2;
            mTexCoords.put ( texIndex + 0, ( float ) j / ( float ) numSlices );
            mTexCoords.put ( texIndex + 1, ( 1.0f - ( float ) i )
                             / ( float ) ( numParallels - 1 ) );
         }
      }

      int index = 0;

      for ( i = 0; i < numParallels; i++ )
      {
         for ( j = 0; j < numSlices; j++ )
         {
            mIndices.put ( index++, ( short ) ( i * ( numSlices + 1 ) + j ) );
            mIndices.put ( index++, ( short ) ( ( i + 1 ) * ( numSlices + 1 ) + j ) );
            mIndices.put ( index++,
                           ( short ) ( ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 ) ) );

            mIndices.put ( index++, ( short ) ( i * ( numSlices + 1 ) + j ) );
            mIndices.put ( index++,
                           ( short ) ( ( i + 1 ) * ( numSlices + 1 ) + ( j + 1 ) ) );
            mIndices.put ( index++, ( short ) ( i * ( numSlices + 1 ) + ( j + 1 ) ) );

         }
      }

      mNumIndices = numIndices;

      return numIndices;
   }

   public int genCube ( float scale )
   {
      int i;
      int numVertices = 24;
      int numIndices = 36;

      float[] cubeVerts = { -0.5f, -0.5f, -0.5f,
                            -0.5f, -0.5f,  0.5f,
                             0.5f, -0.5f,  0.5f,
                             0.5f, -0.5f, -0.5f,
                            -0.5f,  0.5f, -0.5f,
                            -0.5f,  0.5f,  0.5f,
                             0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f,
                            -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
                            -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
                            0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
                            -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
                            0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f,
                          };

      float[] cubeNormals = { 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                              -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                              0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
                              0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                              0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                              1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                              -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                              0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                            };


      //    0, 2, 1, 0, 3, 2,
      float[] cubeTex = {

                          0.0f, 0.01f,    // 0
                          0.0f, 0.33f,   // 1
                          0.35f, 0.33f,   // 2
                         // 0.35f, 0.33f,   // 3
                          0.35f, 0.01f,   // 4
                         // 0.0f, 0.01f,    // 5
                         // 0.3f, 0.0f


              // 4, 5, 6, 4, 6, 7,
                          0.69f, 0.33f,  // 4
                          1.0f, 0.33f,   // 5
                          1.0f, 0.6f,    // 6
                          0.69f, 0.6f,   // 7
                          //0.69f, 0.6f,

                          //1.0f, 0.33f,


              // 8, 9, 10, 8, 10, 11,

                          0.69f, 0.01f,  // 8
                          1.0f, 0.01f,   // 9
                           1.0f, 0.33f,  // 10
                          0.69f, 0.33f,   // 11


             //    12, 15, 14, 12, 14, 13,

                          0.0f, 0.33f,    // 12
                          0.0f, 0.6f,     // 13
                          0.35f, 0.6f,    // 14
                          0.35f, 0.33f,   // 15



              //    16, 17, 18, 16, 18, 19,
                          0.36f, 0.33f,  // 16
                          0.68f, 0.33f,  // 17
                          0.68f, 0.6f,   // 18
                          0.36f, 0.6f, // 19



                          0.36f, 0.01f,  // 20
                          0.68f, 0.01f,  // 21
                          0.68f, 0.33f,  // 22
                          0.36f, 0.33f, // 23



      };

//      float[] cubeTex = { 0.0f, 0.0f,
//                          0.0f, 1.0f,
//                          1.0f, 1.0f,
//                          1.0f, 0.0f,
//                          1.0f, 0.0f,
//                          1.0f, 1.0f,
//                          0.0f, 1.0f,
//                          0.0f, 0.0f,
//                          0.0f, 0.0f, // 10
//                          0.0f, 1.0f,
//                          1.0f, 1.0f,
//                          1.0f, 0.0f,
//                          0.0f, 0.0f,
//                          0.0f, 1.0f,
//                          1.0f, 1.0f,
//                          1.0f, 0.0f,
//                          0.0f, 0.0f,
//                          0.0f, 1.0f,
//                          1.0f, 1.0f,  // 20
//                          1.0f, 0.0f,
//                          0.0f, 0.0f,
//                          0.0f, 1.0f,
//                          1.0f, 1.0f,
//                          1.0f, 0.0f,
//                        };

      // Allocate memory for buffers
      mVertices = ByteBuffer.allocateDirect ( numVertices * 3 * 4 )
                  .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mNormals = ByteBuffer.allocateDirect ( numVertices * 3 * 4 )
                 .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mTexCoords = ByteBuffer.allocateDirect ( numVertices * 2 * 4 )
                   .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
      mIndices = ByteBuffer.allocateDirect ( numIndices * 2 )
                 .order ( ByteOrder.nativeOrder() ).asShortBuffer();

      mVertices.put ( cubeVerts ).position ( 0 );

      for ( i = 0; i < numVertices * 3; i++ )
      {
         mVertices.put ( i, mVertices.get ( i ) * scale );
      }

      mNormals.put ( cubeNormals ).position ( 0 );
      mTexCoords.put ( cubeTex ).position ( 0 );

      short[] cubeIndices = { 0, 2, 1, 0, 3, 2,
                              4, 5, 6, 4, 6, 7,
                              8, 9, 10, 8, 10, 11,
                             12, 15, 14, 12, 14, 13,
                             16, 17, 18, 16, 18, 19,
                             20, 23, 22, 20, 22, 21
                            };

      mIndices.put ( cubeIndices ).position ( 0 );
      mNumIndices = numIndices;
      return numIndices;
   }

   public FloatBuffer getVertices()
   {
      return mVertices;
   }

   public FloatBuffer getNormals()
   {
      return mNormals;
   }

   public FloatBuffer getTexCoords()
   {
      return mTexCoords;
   }

   public ShortBuffer getIndices()
   {
      return mIndices;
   }

   public int getNumIndices()
   {
      return mNumIndices;
   }


   public FloatBuffer getmVerticesPyramid() {
      return mVerticesPyramid;
   }

   public FloatBuffer getmTexCoordsPyramid() {
      return mTexCoordsPyramid;
   }

   // Member variables
   private FloatBuffer mVertices;
   private FloatBuffer mNormals;
   private FloatBuffer mTexCoords;
   private ShortBuffer mIndices;

   private FloatBuffer  mVerticesPyramid;
   private FloatBuffer  mTexCoordsPyramid;

   private int mNumIndices;
}
