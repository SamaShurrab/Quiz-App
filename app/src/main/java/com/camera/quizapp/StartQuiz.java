package com.camera.quizapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class StartQuiz extends AppCompatActivity {

    TextView score,questionNumber,question;
    Button option1,option2,option3,option4,start,exit;
    ProgressBar progress;
    ImageView img;
    ArrayList<String> questionList;
    ArrayList<String> option1List;
    ArrayList<String> option2List;
    ArrayList<String> option3List;
    ArrayList<String> option4List;
    int currentScore=0;
    ArrayList<String> correctAnswerList;
    int currentQuestion=0;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-bddbd-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        //        Change Icons In Status Bar
        View decor= StartQuiz.this.getWindow().getDecorView();
        if(decor.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }//if
        else{
            decor.setSystemUiVisibility(0);
        }//else

        score=findViewById(R.id.startScore);
        questionNumber=findViewById(R.id.question_tv);
        question=findViewById(R.id.startQuestion_tv);
        option1=findViewById(R.id.answer1_btn);
        option2=findViewById(R.id.answer2_btn);
        option3=findViewById(R.id.answer3_btn);
        option4=findViewById(R.id.answer4_btn);
        start=findViewById(R.id.start_btn);
        progress=findViewById(R.id.progress);
        img=findViewById(R.id.imageView_img);
        exit=findViewById(R.id.exit_btn);
        questionList=new ArrayList<>();
        option1List=new ArrayList<>();
        option2List=new ArrayList<>();
        option3List=new ArrayList<>();
        option4List=new ArrayList<>();
        correctAnswerList=new ArrayList<>();
        getData();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.VISIBLE);
                        img.setVisibility(View.VISIBLE);
                    }//run
                },1800);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                        img.setVisibility(View.GONE);
                        score.setVisibility(View.VISIBLE);
                        questionNumber.setVisibility(View.VISIBLE);
                        question.setVisibility(View.VISIBLE);
                        option1.setVisibility(View.VISIBLE);
                        option2.setVisibility(View.VISIBLE);
                        option3.setVisibility(View.VISIBLE);
                        option4.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.VISIBLE);
                        insertDataToViews();
                    }//run
                },3000);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                intent.putExtra("high_score",currentScore);
                startActivity(intent);
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSize()){
                    Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                    intent.putExtra("high_score",currentScore);
                    startActivity(intent);
                }//if
                getCorrectAnswer("option 1");
                score.setText("Score = "+currentScore);
                currentQuestion++;
            }//onclick
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSize()){
                    Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                    intent.putExtra("high_score",currentScore);
                    startActivity(intent);
                }//if
                getCorrectAnswer("option 2");
                score.setText("Score = "+currentScore);
                currentQuestion++;
            }//onclick
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSize()){
                    Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                    intent.putExtra("high_score",currentScore);
                    startActivity(intent);
                }//if
                getCorrectAnswer("option 3");
                score.setText("Score = "+currentScore);
                currentQuestion++;
            }//onclick
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSize()){
                    Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                    intent.putExtra("high_score",currentScore);
                    startActivity(intent);
                }//if
                getCorrectAnswer("option 4");
                score.setText("Score = "+currentScore);
                currentQuestion++;
            }//onClick
        });

    }//onCreate

    public void getData(){
        if(checkInternet()){
            databaseReference.child("Quiz").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        questionList.add(dataSnapshot.child("Question").getValue().toString());
                        option1List.add(dataSnapshot.child("option1").getValue().toString());
                        option2List.add(dataSnapshot.child("option2").getValue().toString());
                        option3List.add(dataSnapshot.child("option3").getValue().toString());
                        option4List.add(dataSnapshot.child("option4").getValue().toString());
                        correctAnswerList.add(dataSnapshot.child("correctAnswer").getValue().toString());
                    }//for
                }//onDataChange
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }else{
            Toast.makeText(StartQuiz.this, getResources().getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();
        }//else
    }//getData()

    public void insertDataToViews(){
        int i=0;
        if(currentQuestion>i){
            i=currentQuestion;
        }//if
        for(;i<questionList.size();){
            questionNumber.setText(getResources().getString(R.string.Question)+" "+(currentQuestion+1));
            question.setText(questionList.get(i));
            option1.setText(option1List.get(i));
            option2.setText(option2List.get(i));
            option3.setText(option3List.get(i));
            option4.setText(option4List.get(i));
            break;
        }//for
    }//insertDataToViews

    public void getCorrectAnswer (String optionNumber){
        int i=0;
        if(currentQuestion>i){
            i=currentQuestion;
        }//if
        for(;i<questionList.size();){
            if(correctAnswerList.get(currentQuestion).equalsIgnoreCase(optionNumber)){
                currentScore=currentScore+3;
                Dialog dialog=new Dialog(StartQuiz.this);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setContentView(R.layout.success_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.getWindow().setWindowAnimations(R.style.AnimationDialog);
                        insertDataToViews();
                        MediaPlayer mediaPlayer=MediaPlayer.create(StartQuiz.this,R.raw.correct);
                        mediaPlayer.start();
                        dialog.show();
                    }//run
                },1800);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(checkSize()){
                            Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                            intent.putExtra("high_score",currentScore);
                            startActivity(intent);
                        }//if
                        dialog.hide();
                    }//run
                },2000);
            }else{
                Dialog dialog=new Dialog(StartQuiz.this);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setContentView(R.layout.wrong_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.getWindow().setWindowAnimations(R.style.AnimationDialog);
                        insertDataToViews();
                        MediaPlayer mediaPlayer=MediaPlayer.create(StartQuiz.this,R.raw.wrong);
                        mediaPlayer.start();
                        dialog.show();
                    }//run
                },1500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(checkSize()){
                            Intent intent=new Intent(StartQuiz.this,MainActivity.class);
                            intent.putExtra("high_score",currentScore);
                            startActivity(intent);
                        }//if
                        dialog.hide();
                    }//run
                },2000);
            }//else
            break;
        }//for
    }//insertDataToViews


    public boolean checkInternet(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            if(networkInfo.isConnected() || networkInfo.isFailover()){
                return true;
            }//if
            else{
                return false;
            }//else
        }else{
            return false;
        }//else
    }//checkInternet()

    public boolean checkSize(){
        boolean check=true;
        if(currentQuestion==questionList.size()){
            check=true;
        }
        else {check=false;}
        return check;
    }//checkSize()

}//StartQuiz