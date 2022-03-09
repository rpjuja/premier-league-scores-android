package com.example.footballscoresapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StandingsListAdapter extends ArrayAdapter<Team> {

    private int resourceLayout;
    private Context mContext;

    public StandingsListAdapter(Context context, int resource, List<Team> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         if (convertView == null) {
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.standings_text_view, null);
         }

         Team team = getItem(position);

         if (team != null) {
             TextView pos = (TextView) convertView.findViewById(R.id.posTextView);
             TextView name = (TextView) convertView.findViewById(R.id.nameTextView);
             TextView gamesPlayed = (TextView) convertView.findViewById(R.id.gamesTextView);
             TextView goals = (TextView) convertView.findViewById(R.id.goalsTextView);
             TextView points = (TextView) convertView.findViewById(R.id.pointsTextView);

             String p = team.getPosition() + ".";
             if (pos != null) {
                 pos.setText(p);
             }
             if (name != null) {
                 name.setText(team.getName());
             }
             if (gamesPlayed != null) {
                 gamesPlayed.setText(team.getGamesPlayed());
             }
             String totGoals = team.getGoalM() + ":" + team.getGoalA();
             if (goals != null) {
                 goals.setText(totGoals);
             }
             if (points != null) {
                 points.setText(team.getPoints());
             }
         }
         return convertView;
    }
}
