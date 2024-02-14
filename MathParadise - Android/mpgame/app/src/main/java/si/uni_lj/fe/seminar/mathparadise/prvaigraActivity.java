package si.uni_lj.fe.seminar.mathparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

// Izbira teme / težavnosti / igre
public class prvaigraActivity extends AppCompatActivity {

    // Deklaracija spremenljivk za *izbrano temo* in *težavnost*
    private String selectedTopicName = "";
    private int tezavnost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prvaigra);

        // Povezava z elementi iz vmesnika
        final LinearLayout Liki = findViewById(R.id.javaLayout);
        final LinearLayout Zaporedje = findViewById(R.id.phpLayout);
        final LinearLayout Računanje = findViewById(R.id.htmlLayout);
        final LinearLayout Napredno = findViewById(R.id.androidLayout);
        final Button startBtn = findViewById(R.id.startQuizBtn);

        // Poslušalci za klik na posamezno temo
        Liki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName = "Liki";
                //tezavnost = 1;

                Liki.setBackgroundResource(R.drawable.round_back_white_stroke10);
                Zaporedje.setBackgroundResource(R.drawable.round_back_white10);
                Računanje.setBackgroundResource(R.drawable.round_back_white10);
                Napredno.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        Zaporedje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName = "Zaporedje";
                //tezavnost = 2;

                Zaporedje.setBackgroundResource(R.drawable.round_back_white_stroke10);
                Liki.setBackgroundResource(R.drawable.round_back_white10);
                Računanje.setBackgroundResource(R.drawable.round_back_white10);
                Napredno.setBackgroundResource(R.drawable.round_back_white10);

            }
        });

        Računanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName = "Osnovno računanje";
                //tezavnost = 3;
                Računanje.setBackgroundResource(R.drawable.round_back_white_stroke10);
                Zaporedje.setBackgroundResource(R.drawable.round_back_white10);
                Liki.setBackgroundResource(R.drawable.round_back_white10);
                Napredno.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        Napredno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopicName = "Napredno";
                //tezavnost = 4;
                Napredno.setBackgroundResource(R.drawable.round_back_white_stroke10);
                Zaporedje.setBackgroundResource(R.drawable.round_back_white10);
                Računanje.setBackgroundResource(R.drawable.round_back_white10);
                Liki.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        // Poslušalec za gumb za začetek kviza
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTopicName.isEmpty()){
                    // Obvestilo, če ni izbrana tema
                    Toast.makeText(prvaigraActivity.this, "Please select topic", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Preusmeritev na aktivnost za kviz
                    //Toast.makeText(prvaigraActivity.this, "Izbral si možnost " + tezavnost, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(prvaigraActivity.this, QuizActivity.class);
                    intent.putExtra("selectedTopic", selectedTopicName);
                    //intent.putExtra("tezavnost", tezavnost);
                    startActivity(intent);

                }
            }
        });
    }
}