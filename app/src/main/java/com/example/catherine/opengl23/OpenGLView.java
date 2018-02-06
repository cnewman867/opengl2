package com.example.catherine.opengl23;

/**
 * Created by catherine on 30/01/18.
 */

import android.opengl.GLSurfaceView;
import android.content.Context;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.AttributeSet;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;

public class OpenGLView extends GLSurfaceView implements GLSurfaceView.Renderer{

    GPUInterface gpuInterface;
    FloatBuffer vbuf;
    float[] modelview;
    float[] perspective;
    float[] cameraPosition;
    float cameraBearing;


    public OpenGLView (Context ctx, AttributeSet as) {
        super(ctx, as);
        setEGLContextClientVersion(2);
        setRenderer(this);
        modelview = new float[16];
        perspective = new float[16];
        cameraPosition = new float[3];

        Matrix.setIdentityM(modelview, 0);
        Matrix.setIdentityM(perspective, 0);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config){
        GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final String vertexShader =
                "attribute vec4 aVertex;\n" +
                        "uniform mat4 uPerspMtx, uMvMtx;\n" +
                        "void main(void)\n" +
                        "{\n" +
                        "gl_Position = uPerspMtx * uMvMtx * aVertex;\n" +
                        "}\n",

                fragmentShader =
                        "precision mediump float;\n" +
                        "uniform vec4 uColour;\n" +
                                "void main(void)\n" +
                                "{\n" +
                                "gl_FragColor = uColour;\n" +
                                "}\n";

        gpuInterface = new GPUInterface(vertexShader, fragmentShader);

        createShapes();
    }


    public void onDrawFrame(GL10 unused) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        gpuInterface.select();

        // check that the shaders have actually compiled!
        if(gpuInterface.checkValid()) {
            Matrix.setIdentityM(modelview, 0);
            Matrix.rotateM(modelview, 0,-cameraBearing, 0, 1, 0 );
            Matrix.translateM(modelview, 0,  -cameraPosition[0], -cameraPosition[1], -cameraPosition[2]);
            gpuInterface.sendMatrix(modelview, "uMvMtx");
            gpuInterface.sendMatrix(perspective, "uPerspMtx");
            gpuInterface.setUniform4fv("uColour", new float[]{1, 0, 0, 1});
            gpuInterface.drawBufferedData(vbuf, 12, "aVertex", 0, 3);
            gpuInterface.setUniform4fv("uColour", new float[]{0.5f, 0, 0, 1});
            gpuInterface.drawBufferedData(vbuf, 12, "aVertex", 3, 3);
            gpuInterface.setUniform4fv("uColour", new float[]{1, 1, 0, 1});
            gpuInterface.drawBufferedData(vbuf, 12, "aVertex", 6, 3);
            gpuInterface.setUniform4fv("uColour", new float[]{0, 1, 0, 1});
            gpuInterface.drawBufferedData(vbuf, 12, "aVertex", 9, 3);
            gpuInterface.setUniform4fv("uColour", new float[]{0,0, 1, 1});
            gpuInterface.drawBufferedData(vbuf, 12, "aVertex", 12, 3);

        }
    }

    public void onSurfaceChanged(GL10 unused, int w, int h) {

        float hFov = 40.0f;
        float aspectRatio = (float) w /h;
        GLES20.glViewport(0, 0, w, h);

        Matrix.perspectiveM(perspective, 0, hFov/aspectRatio, aspectRatio, 0.1f, 100);
    }

    private void createShapes() {
        float[] vertices = {0, 0, -3, 1, 0, -3, 0.5f, 1, -3, -0.5f, 0, -6, 0.5f, 0, -6, 0, 1, -6, 0,0,3, 1, 0, 3, 0.5f, 1, 3,2, 0, -0.5f, 2, 0, 0.5f, 2, 1, 0, -2, 0, -0.5f, -2, 0, 0.5f, -2, 1, 0};

        ByteBuffer vbuf0 = ByteBuffer.allocateDirect(vertices.length * Float.SIZE);
        vbuf0.order(ByteOrder.nativeOrder());
        vbuf = vbuf0.asFloatBuffer();
        vbuf.put(vertices);
        vbuf.position(0);
    }

    public void moveX(float dist) {
        cameraPosition[0] += dist;
    }

    public void moveY(float dist) {
        cameraPosition[1] += dist;
    }

    public void moveZ(float dist) {
        cameraPosition[2] += dist;
    }

    public void rotateBearing(float rot) { cameraBearing += rot; }

    public void moveCamera(float dist) {
        cameraPosition[0] -= dist*Math.sin(cameraBearing * (Math.PI / 180));
        cameraPosition[2] -= dist*Math.cos(cameraBearing * (Math.PI / 180));
    }


}
