package com.camera.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class ShowQuestions extends AppCompatActivity {
    TextView questionNumber,question,wait,answer;
    Button option1,option2,option3,option4,exit,previous,next;
    ImageView waitImg;
    ProgressBar progress;
    ArrayList<String> questionList;
    ArrayList<String> option1List;
    ArrayList<String> option2List;
    ArrayList<String> option3List;
    ArrayList<String> option4List;
    ArrayList<String> correctAnswerList;
    int currentQuestion=0;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-bddbd-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);

        //        Change Icons In Status Bar
        View decor= ShowQuestions.this.getWindow().getDecorView();
        if(decor.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }//if
        else{
            decor.setSystemUiVisibility(0);
        }//else

        questionNumber=findViewById(R.id.Question_tv);
        wait=findViewById(R.id.wait);
        waitImg=findViewById(R.id.imageView_img);
        answer=findViewById(R.id.answer_tv);
        progress=findViewById(R.id.progress);
        question=findViewById(R.id.showQuestion_tv);
        option1=findViewById(R.id.showAnswer1_btn);
        option2=findViewById(R.id.showAnswer2_btn);
        option3=findViewById(R.id.showAnswer3_btn);
        option4=findViewById(R.id.showAnswer4_btn);
        exit=findViewById(R.id.exit_btn);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        questionList=new ArrayList<String>();
        option1List=new ArrayList<String>();
        option2List=new ArrayList<String>();
        option3List=new ArrayList<String>();
        option4List=new ArrayList<String>();
        correctAnswerList=new ArrayList<String>();
        getData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
                wait.setVisibility(View.GONE);
                waitImg.setVisibility(View.GONE);
                answer.setVisibility(View.VISIBLE);
                questionNumber.setVisibility(View.VISIBLE);
                question.setVisibility(View.VISIBLE);
                option1.setVisibility(View.VISIBLE);
                option2.setVisibility(View.VISIBLE);
                option3.setVisibility(View.VISIBLE);
                option4.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
            }
        },4000);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    insertDataToViews();
            }//onclick()
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMove();

            }//onClick()
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShowQuestions.this,MainActivity.class);
                intent.putExtra("high_score",getIntent().getIntExtra("high_score_show",0));
                startActivity(intent);
            }//onClick()
        });
    }//onCreate()

    private void previousMove() {
        if(questionList.size()==0){
            Toast.makeText(ShowQuestions.this, getResources().getString(R.string.no_found_question), Toast.LENGTH_SHORT).show();
        }else{
            for(int i=(currentQuestion-2);i<questionList.size();i--){
                if(i==-1){
                    break;
                }
                questionNumber.setText(getResources().getString(R.string.Question)+" "+(currentQuestion-1));
                question.setText(questionList.get(i));
                option1.setText(option1List.get(i));
                option2.setText(option2List.get(i));
                option3.setText(option3List.get(i));
                option4.setText(option4List.get(i));
                if(correctAnswerList.get(i).equalsIgnoreCase("option 1")){
                    option1.setBackgroundColor(Color.GREEN);
                    option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                } else if (correctAnswerList.get(i).equalsIgnoreCase("option 2")) {
                    option2.setBackgroundColor(Color.GREEN);
                    option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                } else if (correctAnswerList.get(i).equalsIgnoreCase("option 3")) {
                    option3.setBackgroundColor(Color.GREEN);
                    option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                }else {
                    option4.setBackgroundColor(Color.GREEN);
                    option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                    option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                }//else
                currentQuestion-=1;
                break;
            }
        }
    }

    public void getData(){
        if(checkInternet()){
            databaseReference.child("Quiz").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        questionNumber.setText(getResources().getString(R.string.Question)+" "+(currentQuestion+1));
                        if(dataSnapshot.child("Question").getValue().toString()==null){
                            Toast.makeText(ShowQuestions.this, "There is no data to display", Toast.LENGTH_SHORT).show();
                        }else {
                            questionList.add(dataSnapshot.child("Question").getValue().toString());
                            question.setText(questionList.get(0));
                            option1List.add(dataSnapshot.child("option1").getValue().toString());
                            option1.setText(option1List.get(0));
                            option2List.add(dataSnapshot.child("option2").getValue().toString());
                            option2.setText(option2List.get(0));
                            option3List.add(dataSnapshot.child("option3").getValue().toString());
                            option3.setText(option3List.get(0));
                            option4List.add(dataSnapshot.child("option4").getValue().toString());
                            option4.setText(option4List.get(0));
                            correctAnswerList.add(dataSnapshot.child("correctAnswer").getValue().toString());
                            if(correctAnswerList.get(0).equalsIgnoreCase("option 1")){
                                option1.setBackgroundColor(Color.GREEN);
                                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                            } else if (correctAnswerList.get(0).equalsIgnoreCase("option 2")) {
                                option2.setBackgroundColor(Color.GREEN);
                                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                            } else if (correctAnswerList.get(0).equalsIgnoreCase("option 3")) {
                                option3.setBackgroundColor(Color.GREEN);
                                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                            }else {
                                option4.setBackgroundColor(Color.GREEN);
                                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                            }//else
                        }
                    }//for
                    currentQuestion++;
                }//onDataChange
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }else{
            Toast.makeText(ShowQuestions.this, getResources().getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();
        }//else
    }//getData()

    public void insertDataToViews(){
        int i=0;
        if(currentQuestion>i){
            i=currentQuestion;
        }//if
        for(;i<questionList.size();i++){
            questionNumber.setText(getResources().getString(R.string.Question)+" "+(currentQuestion+1));
            question.setText(questionList.get(i));
            option1.setText(option1List.get(i));
            option2.setText(option2List.get(i));
            option3.setText(option3List.get(i));
            option4.setText(option4List.get(i));
            if(correctAnswerList.get(i).equalsIgnoreCase("option 1")){
                option1.setBackgroundColor(Color.GREEN);
                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
            } else if (correctAnswerList.get(i).equalsIgnoreCase("option 2")) {
                option2.setBackgroundColor(Color.GREEN);
                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
            } else if (correctAnswerList.get(i).equalsIgnoreCase("option 3")) {
                option3.setBackgroundColor(Color.GREEN);
                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option4.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
            }else {
                option4.setBackgroundColor(Color.GREEN);
                option2.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option3.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
                option1.setBackground(getResources().getDrawable(R.drawable.qusetions_shape));
            }//else
            currentQuestion++;
            break;
        }//for
    }//insertDataToViews



    public boolean checkInternet(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

}//ShowQuestions