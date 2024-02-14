package si.uni_lj.fe.seminar.mathparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.AsyncTask;
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
import android.content.SharedPreferences;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    private EditText etUporabniskoIme, etGeslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Pridobitev referenc na vmesnike za vnos uporabniškega imena in gesla
        etUporabniskoIme = findViewById(R.id.inputuporabniskoime);
        etGeslo = findViewById(R.id.inputgeslo);

        // Določitev poslušalca za gumb za prijavo
        Button btnLogin = findViewById(R.id.buttonlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pridobitev vnesenega uporabniškega imena in gesla
                String inputuporabniskoime = etUporabniskoIme.getText().toString();
                String inputgeslo = etGeslo.getText().toString();

                // Izvedba prijave
                new LoginTask().execute(inputuporabniskoime, inputgeslo);
            }
        });

        // Določitev poslušalca za besedilo "Imate račun?"
        TextView btn = findViewById(R.id.haveaccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preusmeritev na aktivnost za registracijo
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    // Razred za opravilo prijave v ozadju
    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String inputuporabniskoime = params[0];
                String inputgeslo = params[1];

                // Ustvarjanje URL-ja za prijavo
                URL url = new URL("http://172.20.10.2/mpgame/vaja2/login.php"); // Replace with your actual API URL

                // Odpri povezavo z URL-jem
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Priprava podatkov za pošiljanje na strežnik
                JSONObject postData = new JSONObject();
                postData.put("uporabniskoime", inputuporabniskoime);
                postData.put("geslo", inputgeslo);

                OutputStream os = connection.getOutputStream();
                os.write(postData.toString().getBytes());
                os.flush();
                os.close();

                // Preveri odgovor strežnika
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Prijava uspešna
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                } else {
                    // Prijava neuspešna
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
                try {
                    // Razčleni JSON odgovor
                    JSONObject jsonResponse = new JSONObject(result);

                    if (jsonResponse.has("token")) {
                        String token = jsonResponse.getString("token");
                        String userId = jsonResponse.getString("userId");

                        // Shrani žeton in ID uporabnika v SharedPreferences
                        saveCredentialsToSharedPreferences(token, userId);

                        // Prikaži *TOAST* uspešno prijavo
                        Toast.makeText(LoginActivity.this, "Prijava je bila uspešna!", Toast.LENGTH_SHORT).show();

                        Log.d("LoginActivity", "Token: " + token);
                        Log.d("LoginActivity", "UserId: " + userId);
                        Toast.makeText(LoginActivity.this, "Pozdravljen igralec " + userId, Toast.LENGTH_LONG).show();
                        // Preusmeri na PrvaIgraActivity
                        Intent intent = new Intent(LoginActivity.this, prvaigraActivity.class);
                        startActivity(intent);

                    } else {
                        // Prikaži *TOAST* neuspešno prijavo
                        Toast.makeText(LoginActivity.this, "Prijava ni bila uspešna!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Prijava ni bila uspešna!", Toast.LENGTH_SHORT).show();
            }
        }
        // Shrani žeton in ID uporabnika v SharedPreferences:
        private void saveCredentialsToSharedPreferences(String token, String userId) {
            SharedPreferences sharedPreferences = getSharedPreferences("YourSharedPreferencesFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", token);
            editor.putString("userId", userId);
            editor.apply();
        }
    }
}