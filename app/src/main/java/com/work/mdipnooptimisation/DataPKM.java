package com.work.mdipnooptimisation;

import static android.opengl.GLES30.GL_COMPRESSED_RG11_EAC;
import static android.opengl.GLES30.GL_COMPRESSED_RGBA8_ETC2_EAC;
import static android.opengl.GLES30.GL_COMPRESSED_SIGNED_RG11_EAC;
import static android.opengl.GLES30.GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC;

public class DataPKM {

    byte paddenWidthMSB;
    byte paddenWidthLSB;
    byte paddenHeightMSB;
    byte paddenHeightLSB;
    byte widthMSB;
    byte widthLSB;
    byte heightMSB;
    byte heightLSB;



    DataPKM(){

    }

    DataPKM(byte[] data){

        paddenHeightMSB = data[8];
        paddenWidthLSB = data[9];
        paddenHeightMSB = data[10];
        paddenHeightLSB = data[11];
        widthMSB = data[12];
        widthLSB = data[13];
        heightMSB = data[14];
        heightLSB = data[15];
    }

    short getWidth() {

        return (short) ((widthMSB << 8) | widthLSB);
    }

    short getHeight(){

        return (short) ((heightMSB << 8) | heightLSB);
    }

    short getPaddenWidth(){

        return (short) ((paddenWidthMSB << 8) | paddenWidthLSB);
    }

    short getPaddenHeight() {
         return (short) ((paddenHeightMSB << 8) | paddenHeightLSB);
    }

    int getSize(int internalFormat)
    {

        if(internalFormat != GL_COMPRESSED_RG11_EAC &&
           internalFormat != GL_COMPRESSED_SIGNED_RG11_EAC &&
           internalFormat != GL_COMPRESSED_RGBA8_ETC2_EAC &&
           internalFormat != GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC){
            return (getPaddenWidth() * getPaddenHeight()) >> 1;
        }else {
            return (getPaddenWidth() * getPaddenHeight());
        }
    }


}
