package si.uni_lj.fe.seminar.mathparadise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuizResoults extends AppCompatActivity {

    // Konstanti za prenašanje rezultatov kviza med aktivnostmi
    public static final String EXTRA_CORRECT_ANSWERS = "correct";
    public static final String EXTRA_INCORRECT_ANSWERS = "incorrect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_resoults);

        // Povezava z elementi iz vmesnika.
        final AppCompatButton startNewBtn = findViewById(R.id.startNewQuizBtn);
        final TextView correctAnswer = findViewById(R.id.correctAnswers);
        final TextView incorrectAnswer = findViewById(R.id.incorrectAnswers);

        // Pridobivanje pravilnih in napačnih odgovorov iz prejšnje aktivnosti.
        final int correctAnswers = getIntent().getIntExtra(EXTRA_CORRECT_ANSWERS, 0);
        final int incorrectAnswers = getIntent().getIntExtra(EXTRA_INCORRECT_ANSWERS, 0);

        // Nastavitev besedila pravilnih in napačnih odgovorov v vmesniku. (*UPORABA SAMO PRAVILNIH*)
        correctAnswer.setText(String.valueOf(correctAnswers));
        incorrectAnswer.setText(String.valueOf(incorrectAnswers));

        // Poslušalec za gumb 'Začni nov kviz'.
        startNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResoults.this, prvaigraActivity.class));
                finish();
            }
        });
    }

    // Preusmeritev nazaj na začetni zaslon ob pritisku na gumb 'Nazaj'.
    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizResoults.this, prvaigraActivity.class));
        finish();
    }
}