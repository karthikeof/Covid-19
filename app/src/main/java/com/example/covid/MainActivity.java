package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;



public class MainActivity extends AppCompatActivity {

    /*List<Country> countryList;
    RecyclerView recyclerView;
    CountryAdapter adapter;
    ProgressDialog p;
    List list;*/

    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }


/*
    private class MyTask extends AsyncTask<Void, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            p.setMessage("Loading...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String ActualResult = "";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.coronastatistics.live/countries")
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ActualResult += response.body().string();
                ActualResult += "#";

            } catch (IOException e) {
                e.printStackTrace();
            }
            request = new Request.Builder()
                    .url("https://api.coronastatistics.live/all")
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ActualResult += response.body().string();
                ActualResult += "#";


            } catch (IOException e) {
                e.printStackTrace();
            }
            request = new Request.Builder()
                    .url("https://api.coronastatistics.live/timeline/global")
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ActualResult += response.body().string();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return ActualResult;
        }

        @Override
        protected void onPostExecute(String result) {
            // System.out.println("Hello:"+result);
            JSONObject jsonObj = null;
            String confirmed = "";
            String deaths = "";
            String recovered = "";
            String cntryname = "";
            String todayCases = "";
            String todayDeaths = "";
            String active = "";
            String result1[] = result.split("#");
            try {
                System.out.println(result1[0]);
                JSONArray country = new JSONArray(result1[0]);
                JSONArray sortedJsonArray = new JSONArray();
                list = new ArrayList();
                for (int i = 0; i < country.length(); i++) {
                    list.add(country.getJSONObject(i));
                }
                // Sort("deaths");
               /* Collections.sort(list, new SortBasedOncases()) ;
                for(int i = 0; i < country.length(); i++) {
                    sortedJsonArray.put(list.get(i));
                }*/

             /*   for (int i = 0; i < list.size(); i++) {
                    //JSONObject lhs=(JSONObject)list.get(i);
                    for (int j = 0; j < list.size() - i - 1; j++) {
                        JSONObject lhs = (JSONObject) list.get(j);
                        JSONObject rhs = (JSONObject) list.get(j + 1);

                        if (Long.parseLong(lhs.getString("cases")) < Long.parseLong(rhs.getString("cases"))) {
                            //  System.out.println(Long.parseLong(lhs.getString("cases"))+","+Long.parseLong(rhs.getString("cases")));
                            JSONObject tmp;
                            tmp = (JSONObject) list.get(j);
                            list.set(j, rhs);
                            list.set(j + 1, tmp);

                        }
                    }
                }
                for (int i = 0; i < country.length(); i++) {
                    sortedJsonArray.put(list.get(i));
                }
                for (int i = 0; i < sortedJsonArray.length(); i++) {

                    JSONObject c = sortedJsonArray.getJSONObject(i);


                    confirmed = c.getString("cases");
                    deaths = c.getString("deaths");
                    recovered = c.getString("recovered");
                    cntryname = c.getString("country");
                    todayCases = c.getString("todayCases");
                    todayDeaths = c.getString("todayDeaths");
                    active = c.getString("active");
                    //  System.out.println(confirmed + "   " + deaths + "    " + recovered);
                    if (cntryname.equals("India"))
                        countryList.add(0, new Country(cntryname, confirmed, deaths, recovered, todayCases, todayDeaths, active,"Unsorted"));
                    else
                        countryList.add(new Country(cntryname, confirmed, deaths, recovered, todayCases, todayDeaths, active,"Cases"));
                    //  break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObj1 = new JSONObject(result1[1]);
                String confirmedG = jsonObj1.getString("cases");
                String deathsG = jsonObj1.getString("deaths");
                String recoveredG = jsonObj1.getString("recovered");

                jsonObj1= new JSONObject(result1[2]);
                Iterator<String> keys = jsonObj1.keys();
                String newconfirmedG="0";
                String newdeathsG="0";
                String newrecoveredG="0";
                String key="";

                while(keys.hasNext()) {

                  //keys.next();
                    key=keys.next();


                }

               System.out.println(key);
                JSONObject tmp=(JSONObject) jsonObj1.get(key);


                newconfirmedG=tmp.getString("cases");
                newdeathsG=tmp.getString("deaths");
                newrecoveredG=tmp.getString("recovered");

                System.out.println("newconfirmedG"+newconfirmedG);


                countryList.add(0, new Country("GLOBAL", confirmedG, deathsG, recoveredG,                String.valueOf(Long.parseLong(confirmedG) - Long.parseLong(newconfirmedG))
                        ,                String.valueOf(Long.parseLong(deathsG) - Long.parseLong(newdeathsG))
                        ,                String.valueOf(Long.parseLong(confirmedG) - Long.parseLong(deathsG)-Long.parseLong(recoveredG))
                        ,"Unsorted"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new CountryAdapter(getApplicationContext(), countryList, countryList);

            recyclerView.setAdapter(adapter);
            p.hide();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.SortByDeaths).setVisible(false);
                menu.findItem(R.id.SortByActiveCases).setVisible(false);
                menu.findItem(R.id.SortByRecovered).setVisible(false);
                menu.findItem(R.id.SortByNewCases).setVisible(false);
                menu.findItem(R.id.SortByNewDeath).setVisible(false);


                searchView.clearFocus();
                if (isNetworkConnected() && adapter instanceof CountryAdapter) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(MainActivity.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();

                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menu.findItem(R.id.SortByDeaths).setVisible(false);
                menu.findItem(R.id.SortByActiveCases).setVisible(false);
                menu.findItem(R.id.SortByRecovered).setVisible(false);
                menu.findItem(R.id.SortByNewCases).setVisible(false);
                menu.findItem(R.id.SortByNewDeath).setVisible(false);

                if (isNetworkConnected() && adapter instanceof CountryAdapter) {
                    adapter.getFilter().filter(newText);
                } else {
                    Toast.makeText(MainActivity.this, "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menu.findItem(R.id.SortByDeaths).setVisible(true);
                menu.findItem(R.id.SortByActiveCases).setVisible(true);
                menu.findItem(R.id.SortByRecovered).setVisible(true);
                menu.findItem(R.id.SortByNewCases).setVisible(true);
                menu.findItem(R.id.SortByNewDeath).setVisible(true);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.SortByDeaths: {
                adapter.Sort("Deaths");
              //  Toast.makeText(this, "deaths", Toast.LENGTH_SHORT).show();
                return true;

            }

            case R.id.SortByActiveCases:
                adapter.Sort("Active Cases");
                return true;
            case R.id.SortByRecovered:
                adapter.Sort("Recovered");
                return true;
            case R.id.SortByNewCases:
                adapter.Sort("New Cases");
                return true;
            case R.id.SortByNewDeath:
                adapter.Sort("New Deaths");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    };

        private boolean isNetworkConnected () {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }*/

    }

