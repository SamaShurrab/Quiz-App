package com.camera.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddQuestions extends AppCompatActivity {
    TextView questionNumber;
    EditText question,option1,option2,option3,option4;
    Spinner correctAnswer;
    Button done,next;
    String correctChoice="";
    ArrayList<String> options;
    ProgressBar progress;

    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-bddbd-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);

        //        Change Icons In Status Bar
        View decor= AddQuestions.this.getWindow().getDecorView();
        if(decor.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }//if
        else{
            decor.setSystemUiVisibility(0);
        }//else

        questionNumber=findViewById(R.id.addQuestion_tv);
        question=findViewById(R.id.newQuestion_et);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);
        correctAnswer=findViewById(R.id.correctAnswer);
        progress=findViewById(R.id.progress);
        done=findViewById(R.id.done_btn);
        next=findViewById(R.id.next);
        options=new ArrayList<String>();
        options.add(getResources().getString(R.string.spinner_choose));
        options.add("option 1");
        options.add("option 2");
        options.add("option 3");
        options.add("option 4");
        ArrayAdapter arrayAdapter=new ArrayAdapter(AddQuestions.this,R.layout.options,options);
        arrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
        correctAnswer.setAdapter(arrayAdapter);
        correctAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    correctChoice=correctAnswer.getItemAtPosition(position).toString();
                }//if
            }//onItemSelected()
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty_Question_and_Options()){
                    Toast.makeText(AddQuestions.this, getResources().getString(R.string.question_and_answers_empty), Toast.LENGTH_SHORT).show();
                }else{
                    if(isEmpty_Correct_Answer()){
                        Toast.makeText(AddQuestions.this, getResources().getString(R.string.correct_answer_empty), Toast.LENGTH_SHORT).show();
                    }else{
                        if(checkInternet()){
                            new Handler().postAtTime(new Runnable() {
                                @Override
                                public void run() {
                                  next.setVisibility(View.GONE);
                                  done.setVisibility(View.GONE);
                                  progress.setVisibility(View.VISIBLE);
                                  Toast.makeText(AddQuestions.this, "Moments the question will be added", Toast.LENGTH_SHORT).show();
                                }//run
                            },5000);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    next.setVisibility(View.VISIBLE);
                                    done.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    InsertQuizModel();
                                }//run
                            },4000);
                        }else{
                            Toast.makeText(AddQuestions.this, getResources().getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();
                        }//else
                    }//else
                }//else
            }//onClick()
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty_Correct_Answer()&&!isEmpty_Correct_Answer()){
                    if(checkInternet()){
                           InsertQuizModel();
                    }else{
                        Toast.makeText(AddQuestions.this, getResources().getString(R.string.internet_connect), Toast.LENGTH_SHORT).show();
                    }//else
                }//if
                Intent intent=new Intent(AddQuestions.this,MainActivity.class);
                intent.putExtra("high_score",getIntent().getIntExtra("high_score_add",0));
                startActivity(intent);
            }//onClick()
        });
    }//onCreate()

    public void InsertQuizModel(){
        String questionStr=question.getText().toString();
        String option1Str=option1.getText().toString();
        String option2Str=option2.getText().toString();
        String option3Str=option3.getText().toString();
        String option4Str=option4.getText().toString();
        databaseReference.child("Quiz").child(questionStr).setValue(questionStr);
        databaseReference.child("Quiz").child(questionStr).child("Question").setValue(questionStr);
        databaseReference.child("Quiz").child(questionStr).child("option1").setValue(option1Str);
        databaseReference.child("Quiz").child(questionStr).child("option2").setValue(option2Str);
        databaseReference.child("Quiz").child(questionStr).child("option3").setValue(option3Str);
        databaseReference.child("Quiz").child(questionStr).child("option4").setValue(option4Str);
        databaseReference.child("Quiz").child(questionStr).child("correctAnswer").setValue(correctChoice);
        Toast.makeText(AddQuestions.this, getResources().getString(R.string.success_add), Toast.LENGTH_SHORT).show();
        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
        correctChoice="";
    }//writeNewQuestion()

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

    public boolean isEmpty_Question_and_Options(){
        boolean check=true;
        if(question.getText().toString().isEmpty()||option1.getText().toString().isEmpty()||option2.getText().toString().isEmpty()||option3.getText().toString().isEmpty()
                ||option4.getText().toString().isEmpty()){
            check=true;
        }else{
            check=false;
        }//else
        return check;
    }//isEmpty_Question_and_Options()

    public boolean isEmpty_Correct_Answer(){
        boolean check=true;
        if(correctChoice.isEmpty()){
            check=true;
        }else{
            check=false;
        }//else
        return check;
    }//isEmpty_Correct_Answer()




}//AddQuestions