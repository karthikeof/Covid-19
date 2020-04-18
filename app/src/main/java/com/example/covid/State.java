package com.example.covid;

public class State {
    private String State;
    private String Confirmed;
    private String Death;
    private String Recovered;
    private String todayCases;
    private String todayDeaths;
    private String ActiveCases;
    private String SortBy;



    public State(String State,String Confirmed, String Death, String Recovered, String todayCases, String todayDeaths, String ActiveCases,String SortBy)
    {
        this.State = State;
        this.Confirmed = Confirmed;
        this.Death = Death;
        this.Recovered = Recovered;
        this.todayCases=todayCases;
        this.todayDeaths=todayDeaths;
        this.ActiveCases=ActiveCases;
        this.SortBy=SortBy;

    }
    public void setSortBy(String SortBy)
    {
        this.SortBy=SortBy;
    }

    public String getState() {
        return State;
    }
    public String getConfirmed() {
        return Confirmed;
    }
    public String getDeath() {
        return Death;
    }
    public String getRecovered() {
        return Recovered;
    }
    public String gettodayCases() {
        return todayCases;
    }
    public String gettodayDeaths() {
        return todayDeaths;
    }
    public String getActiveCases() {
        return ActiveCases;
    }
    public String getSortBy(){return SortBy;}
}

