package com.example.footballscoresapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class GamesListAdapter extends ArrayAdapter<Team> {

    private int resourceLayout;
    private Context mContext;

    public GamesListAdapter(Context context, int resource, List<Team> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.games_text_view, null);
        }

        Team team = getItem(position);

        if (team != null) {
            TextView homeName = (TextView) convertView.findViewById(R.id.homeTeamName);
            TextView awayName = (TextView) convertView.findViewById(R.id.awayTeamName);
            TextView homeScore = (TextView) convertView.findViewById(R.id.homeTeamScore);
            TextView awayScore = (TextView) convertView.findViewById(R.id.awayTeamScore);

            if (homeName != null) {
                homeName.setText(team.getHomeTeam());
            }
            if (awayName != null) {
                awayName.setText(team.getAwayTeam());
            }
            if (homeScore != null) {
                homeScore.setText(team.getHomeScore());
            }
            if (awayScore != null) {
                awayScore.setText(team.getAwayScore());
            }
        }
        return convertView;
    }
}