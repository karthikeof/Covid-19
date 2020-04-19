package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    List<District> DistrictList;
    RecyclerView recyclerView;
    DistrictAdapter adapter;
    ProgressDialog p;
    int flag=0;
    List list;
    String[] YourtransferredData;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
         YourtransferredData = intent.getExtras().getString("State").split("#");
        getSupportActionBar().setTitle(YourtransferredData[0].toUpperCase());
        TextView tv = new TextView(getApplicationContext());

        androidx.appcompat.app.ActionBar bar = getSupportActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(YourtransferredData[0].toUpperCase());
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.WHITE);
        tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        tv.setMaxLines(1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setTypeface(null, Typeface.BOLD);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(tv);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDistrict);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefreshDistrict);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkConnected()) {
                            DistrictList= new ArrayList<>();
                            flag=1;
                           new MyTask().execute(true);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(Main2Activity.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       // District tmp=new District("Chennai","10","10","10","Cases");
        DistrictList = new ArrayList<>();
        //DistrictList.add(tmp);

        //adapter = new DistrictAdapter(getApplicationContext(), DistrictList, DistrictList);
       //recyclerView.setAdapter(adapter);
        if (isNetworkConnected()) {
            new MyTask().execute(true);
        }
        else
            Toast.makeText(Main2Activity.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();



    }

    private class MyTask extends AsyncTask<Boolean, Void, String[]> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Main2Activity.this);
            p.setMessage("Loading...");

            p.setIndeterminate(false);
            p.setCancelable(false);
            if(flag==0)
                p.show();


        }

        @Override
        protected String[] doInBackground(Boolean... a) {

            String[] ActualResult = new String[2];
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.covid19india.org/v2/state_district_wise.json")
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ActualResult[0]= response.body().string();
               // ActualResult += "#";

            } catch (IOException e) {
                e.printStackTrace();
            }
             request = new Request.Builder()
                    .url("https://api.covid19india.org/deaths_recoveries.json")
                    .method("GET", null)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ActualResult[1]= response.body().string();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return ActualResult;
        }

        @Override
        protected void onPostExecute(String result[]) {


            try {
                JSONArray newOb=new JSONArray(result[0]);
                HashMap<String, Long> mapc
                        = new HashMap<>();



                for(int i=0;i<newOb.length();i++)
                {
                    if(newOb.getJSONObject(i).getString("state").equals(YourtransferredData[0]))
                    {
                        for(int j=0;j<newOb.getJSONObject(i).getJSONArray("districtData").length();j++)
                        {
                            //mapc.put(newOb.getJSONObject(i).getJSONArray("districtData").getJSONObject(j).getString("district"),Long.parseLong(newOb.getJSONObject(i).getJSONArray("districtData").getJSONObject(j).getString("confirmed")));
                            DistrictList.add(new District(newOb.getJSONObject(i).getJSONArray("districtData").getJSONObject(j).getString("district"),
                                    Long.parseLong(newOb.getJSONObject(i).getJSONArray("districtData").getJSONObject(j).getString("confirmed")),
                                    0L, 0L,
                                    newOb.getJSONObject(i).getJSONArray("districtData").getJSONObject(j).getJSONObject("delta").getString("confirmed"), "Cases"));

                        }
                        break;

                    }
                }

               // for (String key : mapc.keySet()) {
                   // DistrictList.add(new District(key, mapc.get(key), 0L, 0L,"", "Cases"));
                //}

                JSONObject a =new JSONObject(result[1]);
                JSONArray deaths_recoveries=a.getJSONArray("deaths_recoveries");

                for(int i=0;i<deaths_recoveries.length();i++)
                {
                    if(deaths_recoveries.getJSONObject(i).getString("state").equals(YourtransferredData[0]))
                    {
                        String status=deaths_recoveries.getJSONObject(i).getString("patientstatus");
                        String districtname=deaths_recoveries.getJSONObject(i).getString("district");

                        for(int jk=0;jk<DistrictList.size();jk++) {
                            District temp = DistrictList.get(jk);


                            if (temp.getDistrict().trim().equals(districtname.trim())) {

                                if (status.equals("Deceased")) {
                                    temp.setDeath(temp.getDeath() + 1);

                                } else if (status.equals("Recovered")) {
                                    temp.setRecovered((temp.getRecovered() + 1));

                                }


                                DistrictList.set(jk, temp);
                                break;
                            }

                        }



                    }
                }



                //JSONArray country = newOb.getJSONArray("raw_data");

               /* for (int i = 0; i < country.length(); i++) {

                    JSONObject tmp =country.getJSONObject(i);
                    String districtname=tmp.getString("detecteddistrict");
                    if(districtname.equals(""))
                        districtname="Unknown";
                    if(tmp.getString("detectedstate").equals(YourtransferredData[0]))
                    {
                        String status=tmp.getString("currentstatus");



                        int districtflag=0;
                        for(int jk=0;jk<DistrictList.size();jk++)
                        {
                            District temp=DistrictList.get(jk);


                            if(temp.getDistrict().trim().equals(districtname.trim()))
                            {
                                districtflag=1;
if(status.equals("Hospitalized"))
{
temp.setConfirmed(temp.getConfirmed()+1);
}
else if(status.equals("Recovered"))
{
    temp.setConfirmed(temp.getConfirmed()+1);
    temp.setRecovered((temp.getRecovered()+1));
}
else if(status.equals("Deceased"))
{
    temp.setConfirmed(temp.getConfirmed()+1);
    temp.setDeath(temp.getDeath()+1);
}

DistrictList.set(jk,temp);
                                break;

                            }
                        }
                        if(districtflag==0)
                        {
                            if(status.equals("Hospitalized"))
                            {
                                DistrictList.add(new District(districtname, 1L, 0L, 0L, "Cases"));
                            }
                            else if(status.equals("Recovered"))
                            {

                                DistrictList.add(new District(districtname, 1L, 0L, 1L, "Cases"));

                            }
                            else if(status.equals("Deceased"))
                            {

                                DistrictList.add(new District(districtname, 1L, 1L, 0L, "Cases"));

                            }

                        }
                    }
                }*/
                DistrictList.add(0,new District("Total",Long.parseLong(YourtransferredData[1]),Long.parseLong(YourtransferredData[2]),Long.parseLong(YourtransferredData[3]),"","Unsorted"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < DistrictList.size(); i++) {
                //JSONObject lhs=(JSONObject)list.get(i);
                for (int j = 0; j < DistrictList.size() - i - 1; j++) {
                    District lhs = DistrictList.get(j);
                    District rhs =  DistrictList.get(j + 1);

                    if (lhs.getConfirmed() <rhs.getConfirmed()) {
                        District tmp;
                        tmp =  DistrictList.get(j);
                        DistrictList.set(j, rhs);
                        DistrictList.set(j + 1, tmp);

                    }
                }
            }

            /*int kk=0;
for(int i=0;i<DistrictList.size();i++)
    kk+=DistrictList.get(i).getConfirmed();

System.out.println("check"+kk);*/
            adapter = new DistrictAdapter(getApplicationContext(), DistrictList, DistrictList,YourtransferredData[0]);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        //add a HeaderView
           // RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));
            p.hide();


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.SortByDeaths).setVisible(false);
        menu.findItem(R.id.SortByActiveCases).setVisible(false);
        menu.findItem(R.id.SortByRecovered).setVisible(false);
        menu.findItem(R.id.SortByNewCases).setVisible(false);
        menu.findItem(R.id.SortByNewDeath).setVisible(false);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                if (isNetworkConnected() ) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();

                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0) {

                    if (isNetworkConnected()) {
                        adapter.getFilter().filter(newText);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

                    }

                }

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (isNetworkConnected() ) {
                    adapter.getFilter().filter("");
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    private boolean isNetworkConnected () {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
