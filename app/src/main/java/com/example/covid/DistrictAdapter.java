package com.example.covid;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> implements Filterable{


    //this context we will use to inflate the layout
    private Context mCtx;
    String state;

    private List<District> DistrictList,contactListFiltered,DistrictListP;

    public DistrictAdapter(Context mCtx, List<District> DistrictList,List<District> DistrictListP,String state) {
        this.mCtx = mCtx;
        this.DistrictList = DistrictList;
        this.DistrictListP=DistrictListP;
        this.state=state;
    }

    @Override
    public DistrictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_district,null);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  DistrictAdapter.DistrictViewHolder holder, int position) {

        final District District = DistrictList.get(position);
        if(District.getDistrict().equals("Total"))
            holder.textMessage.setText("*Death and recovered may not be accurate for districts");
        else {
            if(District.getNewcases().equals("0"))
            holder.textMessage.setText("");
            else
                holder.textMessage.setText(District.getNewcases() +" NEW CASES");

        }

        //DecimalFormat formatter = new DecimalFormat("#,###");
        //binding the data with the viewholder views
        holder.textDistrict.setText(District.getDistrict());
        holder.textConfirmedCount.setText(String.valueOf(District.getConfirmed()));
        holder.textDeathCount.setText(String.valueOf(District.getDeath()));
        holder.textRecoveredCount.setText(String.valueOf(District.getRecovered()));
        holder.textSorted.setText(District.getSortBy());




        //holder.textSorted.setText(state.getSortBy());
       holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(view.getContext(),"click on item: "+District.getDistrict(),Toast.LENGTH_LONG).show();

                if(District.getDistrict().equals("Total"))
                {
                   // Toast.makeText(view.getContext(),"click on item: "+state,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mCtx.getApplicationContext(), Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Mydata", state+','+"District call");
                    mCtx.startActivity(intent);
                }

                else {
                    Intent intent = new Intent(mCtx, PatientList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("District", District.getDistrict().toString()+","+state);
                    mCtx.startActivity(intent);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return DistrictList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(DistrictListP.size()>0) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = DistrictListP;
                    } else {
                        List<District> filteredList = new ArrayList<>();
                        for (District row : DistrictListP) {

                            if (row.getDistrict().toLowerCase().contains(charString.toLowerCase())) {
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
                DistrictList = (ArrayList<District>) filterResults.values;
                if(DistrictList.size()>0)
                    notifyDataSetChanged();
            }
        };
    }





    class DistrictViewHolder extends RecyclerView.ViewHolder {

        TextView textConfirmedCount, textDeathCount, textRecoveredCount, textDistrict,textSorted,textMessage;
        CardView cardview;

        public DistrictViewHolder(View itemView) {
            super(itemView);

            textDistrict = itemView.findViewById(R.id.textCountryDistrict);
            textConfirmedCount = itemView.findViewById(R.id.textConfirmedCountDistrict);
            textDeathCount = itemView.findViewById(R.id.textDeathCountDistrict);
            textRecoveredCount = itemView.findViewById(R.id.textRecoveredCountDistrict);
            textSorted=itemView.findViewById(R.id.textSortedDistrict);
            cardview=itemView.findViewById(R.id.cardViewd);
            textMessage=itemView.findViewById(R.id.textMessage);


        }
    }
   /* public void Sort(String parameter)  {

        for(int i=0;i< DistrictList.size();i++)
        {
            //JSONObject lhs=(JSONObject)list.get(i);
            for(int j=1;j<DistrictList.size()-i-1;j++)
            {
                District lhs=(District)DistrictList.get(j);
                District rhs=(District)stateList.get(j+1);

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
    }*/

}