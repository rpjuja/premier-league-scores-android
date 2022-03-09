package com.example.footballscoresapp;

public class Team {
    // Variables to show team statistics in standings
    private String name;
    private String position;
    private String gamesPlayed;
    private String points;
    private String goalM;
    private String goalA;
    // Variables to show game statistics
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;

    public Team(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team(String homeTeam, String awayTeam, String homeScore, String awayScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Team(String name, String position, String gamesPlayed, String points, String goalsM, String goalsA) {
        this.name = name;
        this.position = position;
        this.gamesPlayed = gamesPlayed;
        this.points = points;
        this.goalM = goalsM;
        this.goalA = goalsA;
    }

    // Get methods to use in custom list adapters
    public String getName() {
        return name;
    }
    public String getPosition() { return position; }
    public String getGamesPlayed() { return gamesPlayed; }
    public String getPoints() {
        return points;
    }
    public String getGoalM() {
        return goalM;
    }
    public String getGoalA() {
        return goalA;
    }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public String getHomeScore() { return homeScore; }
    public String getAwayScore() { return awayScore; }
}

