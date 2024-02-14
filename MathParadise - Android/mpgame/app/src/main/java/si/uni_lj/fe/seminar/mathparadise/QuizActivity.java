package si.uni_lj.fe.seminar.mathparadise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;



import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Glavni razred aktivnosti za izvajanje kviza
public class QuizActivity extends AppCompatActivity {
    // Deklaracija različnih elementov, kot so besedilo, slike, gumbi, in seznam vprašanj.
    private TextView questions;
    private TextView question;
    private ImageView imagePath;
    private AppCompatButton option1, option2, option3, option4;
    private AppCompatButton nextBtn;
    private Timer quizTimer;
    private int totalTimeInMins = 1;
    private int seconds = 0;
    private List<QuestionsList> questionsLists;

    private int currentQuestionPosition = 0;

    private String selectedOptionByUser = "";
    String getSelectedTopicName;

    // Metoda onCreate, ki se kliče ob začetku aktivnosti
    // Definicija naslova, vprašanja, slike, različnih gumbov izbire, pravilnega/nepravilnega gumba
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Povezava z elementi iz vmesnika.
        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView timer = findViewById(R.id.timer);
        final TextView selectedTopicName = findViewById(R.id.topicName);

        questions = findViewById(R.id.questions);
        question = findViewById(R.id.question);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextBtn = findViewById(R.id.nextBtn);
        imagePath = findViewById(R.id.imagePath);

        // Pridobivanje izbrane teme iz prejšnje aktivnosti.
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");

        // Nastavitev besedila izbrane teme v vmesniku.
        selectedTopicName.setText(getSelectedTopicName);

        // Pridobivanje vprašanj iz QuestionsBank glede na izbrano temo.
        questionsLists = QuestionsBank.getQuestions(getSelectedTopicName);

        // Začetek števca časa za kviz.
        startTimer(timer);

        // Nastavitev besedila za prvo vprašanje in možnosti odgovorov.
        questions.setText((currentQuestionPosition+1)+"/"+questionsLists.size());
        question.setText((questionsLists.get(0).getQuestion()));
        option1.setText(questionsLists.get(0).getOption1());
        option2.setText(questionsLists.get(0).getOption2());
        option3.setText(questionsLists.get(0).getOption3());
        option4.setText(questionsLists.get(0).getOption4());
        imagePath.setImageResource(questionsLists.get(0).getImagePath());


        // Poslušalci za odgovore na možnosti.
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionByUser.isEmpty()){
                    selectedOptionByUser = option1.getText().toString();

                    option1.setBackgroundResource(R.drawable.round_back_red10);
                    option1.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionByUser.isEmpty()){
                    selectedOptionByUser = option2.getText().toString();

                    option2.setBackgroundResource(R.drawable.round_back_red10);
                    option2.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionByUser.isEmpty()){
                    selectedOptionByUser = option3.getText().toString();

                    option3.setBackgroundResource(R.drawable.round_back_red10);
                    option3.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionByUser.isEmpty()){
                    selectedOptionByUser = option4.getText().toString();

                    option4.setBackgroundResource(R.drawable.round_back_red10);
                    option4.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);
                }

            }
        });

        // Poslušalec za gumb 'Naprej'.
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionByUser.isEmpty()){
                    Toast.makeText(QuizActivity.this,"please select the answer",Toast.LENGTH_SHORT).show();
                }
                else{
                    changeNextQuestion();
                }

            }
        });
        // Poslušalec za gumb 'Nazaj'.
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizTimer.purge();
                quizTimer.cancel();

                startActivity(new Intent(QuizActivity.this, prvaigraActivity.class));
                finish();

            }
        });
    }

    // Metoda za prikaz naslednjega vprašanja ali zaključek kviza.
    private void changeNextQuestion(){
        currentQuestionPosition++;

        if((currentQuestionPosition+1) == questionsLists.size()){
            nextBtn.setText("Potrdi izbiro!");
        }

        if((currentQuestionPosition < questionsLists.size())){
            selectedOptionByUser = "";

            option1.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
            option1.setTextColor(Color.parseColor("#1F6BB8"));

            option2.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
            option2.setTextColor(Color.parseColor("#1F6BB8"));

            option3.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
            option3.setTextColor(Color.parseColor("#1F6BB8"));

            option4.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
            option4.setTextColor(Color.parseColor("#1F6BB8"));

            questions.setText((currentQuestionPosition+1)+"/"+questionsLists.size());
            question.setText((questionsLists.get(currentQuestionPosition).getQuestion()));
            option1.setText(questionsLists.get(currentQuestionPosition).getOption1());
            option2.setText(questionsLists.get(currentQuestionPosition).getOption2());
            option3.setText(questionsLists.get(currentQuestionPosition).getOption3());
            option4.setText(questionsLists.get(currentQuestionPosition).getOption4());
            imagePath.setImageResource(questionsLists.get(currentQuestionPosition).getImagePath());

        }
        else{

            // Pošljemo podatke na API
            AddGameTask addGameTask = new AddGameTask(getTokenFromSharedPreferences());
            int mapTopicToDifficulty=mapTopicToDifficulty(getSelectedTopicName);
            addGameTask.execute(getUserIdFromSharedPreferences(), String.valueOf(mapTopicToDifficulty), String.valueOf(getCorrectAnswers()));


            Intent intent = new Intent(QuizActivity.this, QuizResoults.class);
            intent.putExtra("correct", getCorrectAnswers());
            intent.putExtra("incorecct", getInCorrectAnswers());
            startActivity(intent);
            finish();
        }
    }


    //Metoda za zagon števca časa.
    private void startTimer(TextView timerTextView){
        quizTimer = new Timer();

        quizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(seconds == 0 && totalTimeInMins == 0){
                    quizTimer.purge();
                    quizTimer.cancel();
                    Toast.makeText(QuizActivity.this, "Time is over", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuizActivity.this, QuizResoults.class);
                    intent.putExtra("correct", getCorrectAnswers());
                    intent.putExtra("incorrect", getInCorrectAnswers());
                    startActivity(intent);

                    finish();
                }
                else if(seconds == 0){
                    totalTimeInMins--;
                    seconds = 59;
                }
                else{
                    seconds--;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String finalMinutes = String.valueOf(totalTimeInMins);
                        String finalSeconds = String.valueOf(seconds);

                        if(finalMinutes.length() == 1){
                            finalMinutes = "0"+finalMinutes;
                        }

                        if(finalSeconds.length() == 1){
                            finalSeconds = "0"+finalSeconds;
                        }

                        timerTextView.setText(finalMinutes +":"+finalSeconds);
                    }
                });
            }
        }, 1000, 1000);

    }

    // Metoda za pridobitev pravilnih odgovorov.
    private int getCorrectAnswers(){

        int correctAnswers = 0;

        for(int i=0; i<questionsLists.size();i++){

            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelectedAnswer();
            final String getAnswer = questionsLists.get(i).getAnswer();

            if(getUserSelectedAnswer.equals(getAnswer)){
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    // Metoda za pridobitev napačnih odgovorov.
    private int getInCorrectAnswers(){

        int incorrectAnswers = 0;

        for(int i=0; i<questionsLists.size();i++){

            final String getUserSelectedAnswer = questionsLists.get(i).getUserSelectedAnswer();
            final String getAnswer = questionsLists.get(i).getAnswer();

            if(!getUserSelectedAnswer.equals(getAnswer)){
                incorrectAnswers++;
            }
        }

        return incorrectAnswers;
    }

    // Metoda, ki se kliče ob pritisku na gumb za nazaj.
    @Override
    public void onBackPressed() {
        quizTimer.purge();
        quizTimer.cancel();

        startActivity(new Intent(QuizActivity.this, prvaigraActivity.class));
        finish();
    }

    // Metoda za razkritje pravilnega odgovora.
    private void  revealAnswer(){

        final String getAnswer = questionsLists.get(currentQuestionPosition).getAnswer();

        if(option1.getText().toString().equals(getAnswer)){
            option1.setBackgroundResource(R.drawable.round_back_green10);
            option1.setTextColor(Color.WHITE);
        }
        else if(option2.getText().toString().equals(getAnswer)){
            option2.setBackgroundResource(R.drawable.round_back_green10);
            option2.setTextColor(Color.WHITE);
        }
        else if(option3.getText().toString().equals(getAnswer)){
            option3.setBackgroundResource(R.drawable.round_back_green10);
            option3.setTextColor(Color.WHITE);
        }
        else if(option4.getText().toString().equals(getAnswer)){
            option4.setBackgroundResource(R.drawable.round_back_green10);
            option4.setTextColor(Color.WHITE);
        }

    }


    // Metode za pridobivanje žetona in uporabniškega ID-ja iz SharedPreferences.
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("YourSharedPreferencesFile", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("YourSharedPreferencesFile", MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }


    // Dodamo odigrano igro v našo podatkovno bazo.
    public class AddGameTask extends AsyncTask<String, Void, String> {

        private final String API_URL = "http://172.20.10.2/mpgame/vaja2/vseigre.php";
        private final String authToken;

        public AddGameTask(String authToken) {
            this.authToken = authToken;
        }

        @Override
        protected String doInBackground(String... params) {
            if (params.length < 3) {
                return "Invalid parameters";
            }

            String uporabniskoime = params[0];
            String tezavnost = params[1];
            String rezultat = params[2];

            try {
                // Create URL object
                URL url = new URL(API_URL);

                // Open connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + authToken);
                connection.setDoOutput(true);

                // Create JSON object with parameters
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("uporabniskoime", uporabniskoime);
                jsonParams.put("tezavnost", tezavnost);
                jsonParams.put("rezultat", rezultat);

                // Write data to the connection output stream
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(jsonParams.toString());
                os.flush();
                os.close();

                // Get response code
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Read the response if needed
                    InputStream inputStream = connection.getInputStream();
                    // ... Handle the response as per your requirements
                } else {
                    // Handle the error response
                    InputStream errorStream = connection.getErrorStream();
                    // ... Handle the error response as per your requirements
                }

                // Disconnect the connection
                connection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }

            return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle the result as needed
            Log.e("AddGameTask", result);
        }
    }

    // Metoda za preslikavo teme v težavnost.
    private int mapTopicToDifficulty(String topicName) {
        int tezavnost = 0; // Default value

        if ("Liki".equals(topicName)) {
            tezavnost = 1;
        } else if ("Zaporedje".equals(topicName)) {
            tezavnost = 2;
        } else if ("Napredno".equals(topicName)) {
            tezavnost = 4;
        } else {
            tezavnost = 3;
        }

        return tezavnost;
    }

}