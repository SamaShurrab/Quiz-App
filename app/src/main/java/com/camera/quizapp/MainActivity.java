package com.camera.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button startQuiz,addQuestions,showQuestions,zeroHighScore;
    TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Change Icons In Status Bar
        View decor= MainActivity.this.getWindow().getDecorView();
        if(decor.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }//if
        else{
            decor.setSystemUiVisibility(0);
        }//else

        highScore=findViewById(R.id.score);
        startQuiz=findViewById(R.id.button_start);
        addQuestions=findViewById(R.id.button_add);
        showQuestions=findViewById(R.id.button_show);
        zeroHighScore=findViewById(R.id.button_zeroScore);

        Intent intent=getIntent();
        highScore.setText(getResources().getString(R.string.High_Score)+" "+intent.getIntExtra("high_score",0));

        addQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddQuestions.class);
                intent.putExtra("high_score_add",highScore.getText());
                startActivity(intent);
            }
        });

        showQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ShowQuestions.class);
                intent.putExtra("high_score_show",highScore.getText());
                startActivity(intent);
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,StartQuiz.class);
                startActivity(intent);
            }
        });

        zeroHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highScore.setText(getResources().getString(R.string.High_Score)+" "+0);
            }
        });


    }//onCreate()
}//MainActivity