package com.example.covid;

public class District {
    private String District;
    private Long Confirmed;
    private Long Death;
    private Long Recovered;
    private String NewCases;
    private String SortBy;



    public District(String District,Long Confirmed, Long Death, Long Recovered,String NewCases, String SortBy)
    {
        this.District = District;
        this.Confirmed = Confirmed;
        this.Death = Death;
        this.Recovered = Recovered;
        this.SortBy=SortBy;
        this.NewCases=NewCases;

    }
    /*public void setSortBy(String SortBy)
    {
        this.SortBy=SortBy;
    }*/

    public void setConfirmed(Long Confirmed)
    {
        this.Confirmed = Confirmed;

    }
    public void setDeath(Long Death)
    {
        this.Death = Death;

    }
    public void setRecovered(Long Recovered)
    {
        this.Recovered = Recovered;

    }


    public String getDistrict() {
        return District;
    }
    public Long getConfirmed() {
        return Confirmed;
    }
    public Long getDeath() {
        return Death;
    }
    public Long getRecovered() {
        return Recovered;
    }
    public String getSortBy(){return SortBy;}
    public String getNewcases(){return NewCases;}
}

