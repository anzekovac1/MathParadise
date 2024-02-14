package si.uni_lj.fe.seminar.mathparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    // Deklaracija elementov za vnos podatkov
    private EditText etUporabniskoIme, etGeslo, etIme, etPriimek, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Povezava z vmesnikom
        etUporabniskoIme = findViewById(R.id.inputuporabniskoime);
        etGeslo = findViewById(R.id.inputgeslo);
        etIme = findViewById(R.id.inputime);
        etPriimek = findViewById(R.id.inputpriimek);
        etEmail = findViewById(R.id.imputemail);

        // Povezava z gumbom za registracijo
        Button btnRegister = findViewById(R.id.buttonregister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pridobivanje vnesenih podatkov
                String inputuporabniskoime = etUporabniskoIme.getText().toString();
                String inputgeslo = etGeslo.getText().toString();
                String inputime = etIme.getText().toString();
                String inputpriimek = etPriimek.getText().toString();
                String imputemail = etEmail.getText().toString();

                // Izvajanje registracije v ozadju
                new RegistrationTask().execute(inputuporabniskoime, inputgeslo, inputime, inputpriimek, imputemail);
            }
        });

        // Povezava z gumbom, ki omogoča prijavo
        TextView btn = findViewById(R.id.haveaccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preusmeritev na aktivnost za prijavo
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    // Notranji razred za izvajanje registracije prek omrežja
    private class RegistrationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                // Pridobivanje parametrov
                String inputuporabniskoime = params[0];
                String inputgeslo = params[1];
                String inputime = params[2];
                String inputpriimek = params[3];
                String imputemail = params[4];

                // Priprava povezave z API-jem
                URL url = new URL("http://172.20.10.2/mpgame/vaja2/registracija.php"); // Replace with your actual API URL

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Priprava podatkov za pošiljanje na strežnik
                JSONObject postData = new JSONObject();
                postData.put("uporabniskoime", inputuporabniskoime);
                postData.put("geslo", inputgeslo);
                postData.put("ime", inputime);
                postData.put("priimek", inputpriimek);
                postData.put("email", imputemail);

                OutputStream os = connection.getOutputStream();
                os.write(postData.toString().getBytes());
                os.flush();
                os.close();

                // Preverjanje odgovora strežnika
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Registracija uspešna (*TOAST*)
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    // Registracija ni uspela
                    return null;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Obdelava odgovora strežnika
                Toast.makeText(RegisterActivity.this, "Registracija uspešna, sedaj se lahko prijavite!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            } else {
                Toast.makeText(RegisterActivity.this, "Uporabniško ime ali email je že v uporabi!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}