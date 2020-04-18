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

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> implements Filterable{


    //this context we will use to inflate the layout
    private Context mCtx;

    private List<State> stateList,contactListFiltered,stateListP;

    public StateAdapter(Context mCtx, List<State> stateList,List<State> stateListP) {
        this.mCtx = mCtx;
        this.stateList = stateList;
        this.stateListP=stateListP;
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_card_state,null);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  StateAdapter.StateViewHolder holder, int position) {

        final State state = stateList.get(position);
        //DecimalFormat formatter = new DecimalFormat("#,###");
        //binding the data with the viewholder views
        holder.textstate.setText(state.getState());
        holder.textConfirmedCount.setText(state.getConfirmed());
        holder.textDeathCount.setText(state.getDeath());
        holder.textRecoveredCount.setText(state.getRecovered());
        /*if(Long.parseLong(state.gettodayCases())==0)
      holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
            holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_trending_up_black_14dp,0);*/
        holder.todayCases.setText(" :"+ state.gettodayCases());


        /*if(Long.parseLong(state.gettodayDeaths())==0)
            holder.todayDeaths.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.todayCases.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_timeline_black_24dp,0);*/
        holder.todayDeaths.setText(" :"+state.gettodayDeaths());



        /*if(Long.parseLong(state.getActiveCases())==0)
            holder.active.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            holder.active.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_black_24dp, 0);*/
        holder.active.setText(" :"+state.getActiveCases());


        holder.textSorted.setText(state.getSortBy());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(view.getContext(),"click on item: "+state.getState(),Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager) mCtx.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if(state.getState().equals("Total"))
                {
                    Intent intent = new Intent(mCtx.getApplicationContext(), Dashboard.class);
                    intent.putExtra("Mydata", "India"+','+state.getConfirmed()+','+state.getDeath()+','+state.getRecovered());
                    mCtx.startActivity(intent);
                }
                else {

                    Intent intent = new Intent(mCtx.getApplicationContext(), Main2Activity.class);
                    intent.putExtra("State", state.getState().toString()+"#"+state.getConfirmed()+"#"+state.getDeath()+"#"+state.getRecovered());
                    mCtx.startActivity(intent);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return stateList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(stateListP.size()>0) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = stateListP;
                    } else {
                        List<State> filteredList = new ArrayList<>();
                        for (State row : stateListP) {

                            if (row.getState().toLowerCase().contains(charString.toLowerCase())) {
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
                stateList = (ArrayList<State>) filterResults.values;
                if(stateList.size()>0)
                    notifyDataSetChanged();
            }
        };
    }





    class StateViewHolder extends RecyclerView.ViewHolder {

        TextView textConfirmedCount, textDeathCount, textRecoveredCount, textstate,todayCases,todayDeaths,active,textSorted;
        CardView cardview;

        public StateViewHolder(View itemView) {
            super(itemView);

            textstate = itemView.findViewById(R.id.textCountryState);
            textConfirmedCount = itemView.findViewById(R.id.textConfirmedCountState);
            textDeathCount = itemView.findViewById(R.id.textDeathCountState);
            textRecoveredCount = itemView.findViewById(R.id.textRecoveredCountState);
            todayCases=itemView.findViewById(R.id.NoOfNewCasesCountState);
            todayDeaths=itemView.findViewById(R.id.NoOfNewDeathsCountState);
            active=itemView.findViewById(R.id.NoOfNewRecoveriescountState);
            textSorted=itemView.findViewById(R.id.textSortedState);
            cardview=itemView.findViewById(R.id.cardViews);


        }
    }
    public void Sort(String parameter)  {

        for(int i=0;i< stateList.size();i++)
        {
            //JSONObject lhs=(JSONObject)list.get(i);
            for(int j=1;j<stateList.size()-i-1;j++)
            {
                State lhs=(State)stateList.get(j);
                State rhs=(State)stateList.get(j+1);

                if(parameter.equals("Deaths")) {
                    if (Long.parseLong(lhs.getDeath()) < Long.parseLong(rhs.getDeath())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        State tmp;
                        tmp =  stateList.get(j);
                        stateList.set(j, rhs);
                        stateList.set(j + 1, tmp);

                    }
                }
                else if(parameter.equals("Active Cases")) {
                    if (Long.parseLong(lhs.getActiveCases()) < Long.parseLong(rhs.getActiveCases())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        State tmp;
                        tmp =  stateList.get(j);
                        stateList.set(j, rhs);
                        stateList.set(j + 1, tmp);

                    }
                }
                else if(parameter.equals("Recovered")) {
                    if (Long.parseLong(lhs.getRecovered()) < Long.parseLong(rhs.getRecovered())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        State tmp;
                        tmp =  stateList.get(j);
                        stateList.set(j, rhs);
                        stateList.set(j + 1, tmp);

                    }
                }
                else if(parameter.equals("New Cases")) {
                    if (Long.parseLong(lhs.gettodayCases()) < Long.parseLong(rhs.gettodayCases())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        State tmp;
                        tmp =  stateList.get(j);
                        stateList.set(j, rhs);
                        stateList.set(j + 1, tmp);

                    }
                }
                else if(parameter.equals("New Deaths")) {
                    if (Long.parseLong(lhs.gettodayDeaths()) < Long.parseLong(rhs.gettodayDeaths())) {
                        //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                        State tmp;
                        tmp =  stateList.get(j);
                        stateList.set(j, rhs);
                        stateList.set(j + 1, tmp);

                    }
                }

            }
        }
        for(int j=1;j<stateList.size();j++)
        {
            stateList.get(j).setSortBy(parameter);
        }
        notifyDataSetChanged();
    }

}