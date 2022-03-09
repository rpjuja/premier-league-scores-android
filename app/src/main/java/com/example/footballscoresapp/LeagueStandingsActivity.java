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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeagueStandingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_standings);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.standingsScreenSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Use this class to listen to what item is selected
        spinner.setOnItemSelectedListener(this);

        String apiKey = "6caf9648db5c47c09dc321b9b7aefce3";
        // Get standings as a json object
        String url = "https://api.football-data.org/v2/competitions/PL/standings";
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Hide text from spinner
        if (view != null) {
            ((TextView) view).setText(null);
        }
        // An item was selected
        switch(pos) {
            case 1:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
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

    public void parseJson(JSONObject response) {
        String teamName, position, points, gamesPlayed, goalsM, goalsA;
        List<Team> teamList = new ArrayList<>();
        try {
            // Get league standings to an array
            JSONArray standingsArray = response.getJSONArray("standings").getJSONObject(0).getJSONArray("table");
            for (int i = 0; i < standingsArray.length(); i++) {
                // Iterate through the array and get all needed variables from each object
                JSONObject teamObject = standingsArray.getJSONObject(i);
                teamName = teamObject.getJSONObject("team").getString("name");
                position = teamObject.getString("position");
                points = teamObject.getString("points");
                gamesPlayed = teamObject.getString("playedGames");
                goalsM = teamObject.getString("goalsFor");
                goalsA = teamObject.getString("goalsAgainst");
                // Add team information to array list
                teamList.add(i, new Team(teamName, position ,gamesPlayed, points, goalsM, goalsA));
            }

            // Find listView by id
            ListView listView = findViewById(R.id.listView);
            // Use a custom list adapter and a and custom layout to set the content of listview
            StandingsListAdapter customAdapter = new StandingsListAdapter(this, R.layout.standings_text_view, teamList);
            listView.setAdapter(customAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}