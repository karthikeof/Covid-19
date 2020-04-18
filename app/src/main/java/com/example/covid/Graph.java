package com.example.covid;

public class Graph{
private Long Confirmed;
private Long Death;
private Long Recovered;
 private String Date;

public Graph(Long Confirmed, Long Death, Long Recovered, String Date)
        {
        this.Confirmed = Confirmed;
        this.Death = Death;
        this.Recovered = Recovered;
        this.Date=Date;

        }
    /*public void setSortBy(String SortBy)
    {
        this.SortBy=SortBy;
    }*/





public Long getConfirmed() {
        return Confirmed;
        }
public Long getDeath() {
        return Death;
        }
public Long getRecovered() {
        return Recovered;
        }
public String getDate(){return Date;}
        }
