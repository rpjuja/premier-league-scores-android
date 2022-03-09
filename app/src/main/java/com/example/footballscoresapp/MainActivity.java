package com.example.footballscoresapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String gameStatusScheduled = "SCHEDULED,LIVE,IN_PLAY,PAUSED,POSTPONED,SUSPENDED";
    private final String gameStatusFinished = "FINISHED,CANCELED";
    private String gameStatus = gameStatusScheduled;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if( savedInstanceState != null) {
            // Extract currently viewed game status from bundle
            String statusText = savedInstanceState.getString("GAME_STATUS", gameStatusScheduled);
            gameStatus = statusText;
            // Set button text to same
            String defaultString = getString(R.string.gameStatusScheduled);
            String buttonText =  savedInstanceState.getString("BUTTON_TEXT", defaultString);
            TextView textView = findViewById(R.id.textView);
            textView.setText(buttonText);
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mainScreenSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Use this class to listen to what item is selected from the spinner
        spinner.setOnItemSelectedListener(this);
        fetchData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Possible activity data to save between lifecycle events
        String statusText = gameStatus;
        outState.putString("GAME_STATUS", statusText);
        TextView textView = findViewById(R.id.textView);
        String buttonText = textView.getText().toString();
        outState.putString("BUTTON_TEXT", buttonText);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Hide text from spinner
        if (view != null) {
            ((TextView) view).setText(null);
        }
        // An item was selected
        switch (pos) {
            case 1:
                Intent standingsIntent = new Intent(this, LeagueStandingsActivity.class);
                startActivity(standingsIntent);
                break;
            case 2:
                Intent scoringIntent = new Intent(this, LeagueScoringActivity.class);
                startActivity(scoringIntent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void fetchData() {
        String apiKey = "6caf9648db5c47c09dc321b9b7aefce3";
        // Get scheduled and ongoing matches as a json object
        String url = "https://api.football-data.org/v2/competitions/PL/matches?status=" + gameStatus;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                this::parseJson,
                Throwable::printStackTrace) {
            // Passing the api key as a header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Auth-Token", apiKey);
                return headers;
            }
        };
        queue = Volley.newRequestQueue(this);
        queue.add(jsonRequest);
    }

    public void parseJson(JSONObject response) {
        String homeTeam, awayTeam, homeScore, awayScore;
        List<Team> itemList = new ArrayList<>();
        try {
            // Get array of games and save to a variable
            JSONArray gameList = response.getJSONArray("matches");
            for (int i = 0; i < gameList.length(); i++) {
                // Iterate through json array and get info from each match
                JSONObject gameObject = gameList.getJSONObject(i);
                homeTeam = gameObject.getJSONObject("homeTeam").getString("name");
                awayTeam = gameObject.getJSONObject("awayTeam").getString("name");
                homeScore = gameObject.getJSONObject("score").getJSONObject("fullTime").getString("homeTeam");
                awayScore = gameObject.getJSONObject("score").getJSONObject("fullTime").getString("awayTeam");
                // Add match information to array list
                // If game hasn't been played/started yet, don't use scores
                if (homeScore.equals("null") || awayScore.equals("null")) {
                    itemList.add(i, new Team(homeTeam, awayTeam));
                } else {
                    itemList.add(i, new Team(homeTeam, awayTeam, homeScore, awayScore));
                }
            }
            // Reverse list if viewing finished games so the latest game comes first
            if (gameStatus.equals(gameStatusFinished)) {
                Collections.reverse(itemList);
            }
            // Find listView by id
            ListView listView = findViewById(R.id.listView);
            // Use a custom adapter to set content of listview and use a custom layout to change the font size
            GamesListAdapter customAdapter = new GamesListAdapter(this, R.layout.games_text_view, itemList);
            listView.setAdapter(customAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeGameStatus(View view) {
        // Switch parameters,
        if (gameStatus.equals(gameStatusScheduled)) {
            gameStatus = gameStatusFinished;
            TextView textView = findViewById(R.id.textView);
            String string = getString(R.string.gameStatusFinished);
            textView.setText(string);
        } else {
            gameStatus = gameStatusScheduled;
            TextView textView = findViewById(R.id.textView);
            String string = getString(R.string.gameStatusScheduled);
            textView.setText(string);
        }
        // Fetch again with different parameters
        fetchData();
    }

}