package com.example.covid;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OneFragment extends Fragment {
    List<Country> countryList;
    RecyclerView recyclerView;
    CountryAdapter adapter;
    ProgressDialog p;
    int flag=0;
    List list;
    public OneFragment() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static OneFragment newInstance() {
        return new OneFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_one, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        final SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        //swipeRefreshLayout.set
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkConnected()) {
                            countryList= new ArrayList<>();
                            flag=1;
                            new MyTask().execute(false);
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }
                }
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        countryList = new ArrayList<>();


        if (isNetworkConnected()) {
            new MyTask().execute(true);
        } else {
            Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();

        }
        //final TextView textView = root.findViewById(R.id.fragment1);
        /*pageViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    private class MyTask extends AsyncTask<Boolean, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getActivity());
            p.setMessage("Loading...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            if(flag==0)
            p.show();


        }

        @Override
        protected String doInBackground(Boolean... a) {

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

               for (int i = 0; i < list.size(); i++) {
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
                    if (cntryname.equals("India")) {
                       // countryList.add(0, new Country(cntryname, confirmed, deaths, recovered, todayCases, todayDeaths, active, "Unsorted"));

                    }
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

            adapter = new CountryAdapter(getActivity(), countryList, countryList);

            recyclerView.setAdapter(adapter);
            //if(a[])
            p.hide();


        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
       // MenuInflater inflater = getMenuInflater();
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        menu.findItem(R.id.SortByDeaths).setVisible(true);
        menu.findItem(R.id.SortByActiveCases).setVisible(true);
        menu.findItem(R.id.SortByRecovered).setVisible(true);
        menu.findItem(R.id.SortByNewCases).setVisible(true);
        menu.findItem(R.id.SortByNewDeath).setVisible(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

              //  Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                menu.findItem(R.id.SortByDeaths).setVisible(false);
                menu.findItem(R.id.SortByActiveCases).setVisible(false);
                menu.findItem(R.id.SortByRecovered).setVisible(false);
                menu.findItem(R.id.SortByNewCases).setVisible(false);
                menu.findItem(R.id.SortByNewDeath).setVisible(false);
                searchView.clearFocus();


               // searchView.clearFocus();
                if (isNetworkConnected() && adapter instanceof CountryAdapter) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();

                }
                menu.findItem(R.id.SortByDeaths).setVisible(true);
                menu.findItem(R.id.SortByActiveCases).setVisible(true);
                menu.findItem(R.id.SortByRecovered).setVisible(true);
                menu.findItem(R.id.SortByNewCases).setVisible(true);
                menu.findItem(R.id.SortByNewDeath).setVisible(true);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length()>0) {
                    //Toast.makeText(getActivity(), "Hi2", Toast.LENGTH_SHORT).show();

                    menu.findItem(R.id.SortByDeaths).setVisible(false);
                    menu.findItem(R.id.SortByActiveCases).setVisible(false);
                    menu.findItem(R.id.SortByRecovered).setVisible(false);
                    menu.findItem(R.id.SortByNewCases).setVisible(false);
                    menu.findItem(R.id.SortByNewDeath).setVisible(false);

                    if (isNetworkConnected() && adapter instanceof CountryAdapter) {
                        adapter.getFilter().filter(newText);
                    } else {
                        Toast.makeText(getActivity(), "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

                    }
               /* menu.findItem(R.id.SortByDeaths).setVisible(true);
                menu.findItem(R.id.SortByActiveCases).setVisible(true);
                menu.findItem(R.id.SortByRecovered).setVisible(true);
                menu.findItem(R.id.SortByNewCases).setVisible(true);
                menu.findItem(R.id.SortByNewDeath).setVisible(true);*/
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                if (isNetworkConnected() && adapter instanceof CountryAdapter) {
                    adapter.getFilter().filter("");
                } else {
                    Toast.makeText(getActivity(), "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

                }

                menu.findItem(R.id.SortByDeaths).setVisible(true);
                menu.findItem(R.id.SortByActiveCases).setVisible(true);
                menu.findItem(R.id.SortByRecovered).setVisible(true);
                menu.findItem(R.id.SortByNewCases).setVisible(true);
                menu.findItem(R.id.SortByNewDeath).setVisible(true);
                return false;
            }
        });


        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isNetworkConnected() && adapter instanceof CountryAdapter) {

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
        }
        else {
            Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();

            return false;
        }
    };
    private boolean isNetworkConnected () {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}