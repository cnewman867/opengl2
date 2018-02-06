package com.example.catherine.opengl23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button x1 = (Button)findViewById(R.id.btn1);
        x1.setOnClickListener(this);
        Button y1 = (Button)findViewById(R.id.btn2);
        y1.setOnClickListener(this);
        Button z1 = (Button)findViewById(R.id.btn3);
        z1.setOnClickListener(this);
        Button x2 = (Button)findViewById(R.id.btn4);
        x2.setOnClickListener(this);
        Button y2 = (Button)findViewById(R.id.btn5);
        y2.setOnClickListener(this);
        Button z2 = (Button)findViewById(R.id.btn6);
        z2.setOnClickListener(this);
        Button rotA = (Button)findViewById(R.id.btn7);
        rotA.setOnClickListener(this);
        Button rotB = (Button)findViewById(R.id.btn8);
        rotB.setOnClickListener(this);
        Button mvBack = (Button)findViewById(R.id.btn9);
        mvBack.setOnClickListener(this);
        Button mvFwd = (Button)findViewById(R.id.btn10);
        mvFwd.setOnClickListener(this);
    }

    public void onClick(View view) {
        OpenGLView ogl = findViewById(R.id.ogl);
        if(view.getId() == R.id.btn1) {
            ogl.moveX(1);
        } else if (view.getId() == R.id.btn4) {
            ogl.moveX(-1);
        } else if (view.getId()  == R.id.btn2) {
            ogl.moveY(1);
        } else if (view.getId() == R.id.btn5) {
            ogl.moveY(-1);
        } else if (view.getId() == R.id.btn3) {
            ogl.moveZ(1);
        } else if(view.getId() == R.id.btn6) {
            ogl.moveZ(-1);
        } else if(view.getId() == R.id.btn7) {
            ogl.rotateBearing( 10 );
        } else if(view.getId() == R.id.btn8) {
            ogl.rotateBearing( -10 );
        } else if(view.getId() == R.id.btn9) {
            ogl.moveCamera( -1 );
        } else if(view.getId() == R.id.btn10) {
            ogl.moveCamera( 1 );
        }
    }
}
