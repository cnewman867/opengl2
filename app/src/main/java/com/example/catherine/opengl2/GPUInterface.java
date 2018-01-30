package com.example.catherine.opengl2;

/**
 * Created by catherine on 30/01/18.
 */
import android.opengl.GLES20;
import java.nio.Buffer;

import android.util.Log;


public class GPUInterface {

    int vertexShader = -1;
    int fragmentShader = -1;
    int shaderProgram = -1;

    public  GPUInterface(String vertexShaderCode, String fragmentShaderCode) {
        if((vertexShader = addVertexShader(vertexShaderCode)) >= 0)
            if ((fragmentShader = addFragmentShader(fragmentShaderCode)) >= 0)
                shaderProgram = makeProgram(vertexShader, fragmentShader);
    }

    public int addVertexShader(String shaderCode) {
        return getShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public int addFragmentShader(String shaderCode) {
        return getShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int getShader(int shaderType, String shaderCode) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if(compileStatus[0] == 0) {
            Log.e("OpenGL", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return -1;
        }

        return shader;
    }

    public static int makeProgram(int vertexShader, int fragmentShader) {

        int shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        int[] linkStatus = new int[1];

        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if(linkStatus[0] == 0) {
            Log.e("OpenGL", "Error linking shader program " + GLES20.glGetProgramInfoLog(shaderProgram));
            GLES20.glDeleteProgram(shaderProgram);
            return -1;
        }

        GLES20.glUseProgram(shaderProgram);
        return shaderProgram;
    }

    public void drawBufferedData(Buffer vertices, int stride, String attrVar, int vertexStart, int nVertices) {

        if(isValid()) {
            int attrVarRef = getShaderVarRef(attrVar);
            vertices.position(0);

            GLES20.glEnableVertexAttribArray(attrVarRef);
            GLES20.glVertexAttribPointer(attrVarRef, 3, GLES20.GL_FLOAT, false, stride, vertices);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, vertexStart, nVertices);
        }
    }

    public int getShaderVarRef(String shaderVar) {
        int refAttrVar = isValid() ? GLES20.glGetAttribLocation(shaderProgram, shaderVar) : -1;
        return refAttrVar;
    }

    public void sendMatrix(float[] mtx, String shaderMtxVar) {
        if(isValid()) {
            int refMtxVar = GLES20.glGetUniformLocation(shaderProgram, shaderMtxVar);
            GLES20.glUniformMatrix4fv(refMtxVar, 1, false, mtx, 0);
        }
    }

    public void setUniform4fv(String shaderVar, float[] val) {
        if(isValid()) {
            int refShaderVar = GLES20.glGetUniformLocation(shaderProgram, shaderVar);
            GLES20.glUniform4fv(refShaderVar, 1, val, 0);
        }
    }


    public void select() {
        GLES20.glUseProgram(shaderProgram);
    }

    public boolean isValid() {
        return shaderProgram >= 0;
    }
}
