package com.example.covid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable{


//this context we will use to inflate the layout
private Context mCtx;

private List<Country> CountryList,contactListFiltered,CountryListP;

public CountryAdapter(Context mCtx, List<Country> CountryList,List<Country> CountryListP) {
        this.mCtx = mCtx;
        this.CountryList = CountryList;
        this.CountryListP=CountryListP;
        }

@Override
public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_card,null);

        return new CountryViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.CountryViewHolder holder, int position) {
        final Country country = CountryList.get(position);
        //DecimalFormat formatter = new DecimalFormat("#,###");
        //binding the data with the viewholder views
        holder.textCountry.setText(country.getCountry());
        holder.textConfirmedCount.setText(country.getConfirmed());
        holder.textDeathCount.setText(country.getDeath());
        holder.textRecoveredCount.setText(country.getRecovered());
        holder.todayCases.setText(" :"+ country.gettodayCases());
       /* if(Long.parseLong(country.gettodayCases())==0)
            holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_trending_up_black_14dp,0);*/


        holder.todayDeaths.setText(" :"+country.gettodayDeaths());
       /* if(Long.parseLong(country.gettodayDeaths())==0)
            holder.todayDeaths.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_timeline_black_24dp,0);*/


        holder.active.setText(" :"+country.getActiveCases());

        /*if(Long.parseLong(country.getActiveCases())==0)
            holder.active.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.active.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_black_24dp, 0);*/


        holder.textSorted.setText(country.getSortBy());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(view.getContext(),"click on item: "+country.getCountry(),Toast.LENGTH_LONG).show();

                //mCtx.searchView.clearFocus();
                InputMethodManager imm = (InputMethodManager) mCtx.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Intent intent = new Intent(mCtx.getApplicationContext(), Dashboard.class);
                intent.putExtra("Mydata", country.getCountry()+','+country.getConfirmed()+','+country.getDeath()+','+country.getRecovered());
                mCtx.startActivity(intent);
            }
        });
    }



@Override
public int getItemCount() {
        return CountryList.size();
        }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(CountryListP.size()>0) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = CountryListP;
                    } else {
                        List<Country> filteredList = new ArrayList<>();
                        for (Country row : CountryListP) {

                            if (row.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }
                else
                    return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                CountryList = (ArrayList<Country>) filterResults.values;
                if(CountryList.size()>0)
                notifyDataSetChanged();
            }
        };
    }





    class CountryViewHolder extends RecyclerView.ViewHolder {

    TextView textConfirmedCount, textDeathCount, textRecoveredCount, textCountry,todayCases,todayDeaths,active,textSorted;
    ImageView imageView;
    CardView cardview;

    public CountryViewHolder(View itemView) {
        super(itemView);

        textCountry = itemView.findViewById(R.id.textCountry);
        textConfirmedCount = itemView.findViewById(R.id.textConfirmedCount);
        textDeathCount = itemView.findViewById(R.id.textDeathCount);
        textRecoveredCount = itemView.findViewById(R.id.textRecoveredCount);
        todayCases=itemView.findViewById(R.id.NoOfNewCasesCount);
        todayDeaths=itemView.findViewById(R.id.NoOfNewDeathsCount);
        active=itemView.findViewById(R.id.NoOfNewRecoveriescount);
        textSorted=itemView.findViewById(R.id.textSorted);
        cardview=itemView.findViewById(R.id.cardView);

    }
}
    public void Sort(String parameter)  {

        for(int i=0;i< CountryList.size();i++)
        {
            //JSONObject lhs=(JSONObject)list.get(i);
            for(int j=2;j<CountryList.size()-i-1;j++)
            {
                Country lhs=(Country)CountryList.get(j);
                Country rhs=(Country)CountryList.get(j+1);

               // System.out.println("Country"+lhs.getCountry());
                //System.out.println("Country1"+rhs.getCountry());


                if(parameter.equals("Deaths")) {
                    if (Long.parseLong(lhs.getDeath()) < Long.parseLong(rhs.getDeath())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        Country tmp;
                        tmp =  CountryList.get(j);
                        CountryList.set(j, rhs);
                        CountryList.set(j + 1, tmp);

                    }
                }
                else if(parameter.equals("Active Cases")) {
                    if (Long.parseLong(lhs.getActiveCases()) < Long.parseLong(rhs.getActiveCases())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        Country tmp;
                        tmp =  CountryList.get(j);
                        CountryList.set(j, rhs);
                        CountryList.set(j + 1, tmp);

                    }
                }
               else if(parameter.equals("Recovered")) {
                    if (Long.parseLong((lhs.getRecovered().equals("null")?"0":lhs.getRecovered())) < Long.parseLong((rhs.getRecovered().equals("null")?"0":rhs.getRecovered()))) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        Country tmp;
                        tmp =  CountryList.get(j);
                        CountryList.set(j, rhs);
                        CountryList.set(j + 1, tmp);

                    }
                }
               else if(parameter.equals("New Cases")) {
                    if (Long.parseLong(lhs.gettodayCases()) < Long.parseLong(rhs.gettodayCases())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        Country tmp;
                        tmp =  CountryList.get(j);
                        CountryList.set(j, rhs);
                        CountryList.set(j + 1, tmp);

                    }
                }
               else if(parameter.equals("New Deaths")) {
                    if (Long.parseLong(lhs.gettodayDeaths()) < Long.parseLong(rhs.gettodayDeaths())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        Country tmp;
                        tmp =  CountryList.get(j);
                        CountryList.set(j, rhs);
                        CountryList.set(j + 1, tmp);

                    }
                }

            }
        }
        for(int j=2;j<CountryList.size();j++)
        {
            CountryList.get(j).setSortBy(parameter);
        }
        notifyDataSetChanged();
    }

}
