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
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TwoFragment extends Fragment {
    List<State> StateList;
    RecyclerView recyclerView;
    StateAdapter adapter;
    int flag=0;
    List list;
    ProgressDialog p;

    public TwoFragment() {
        // Required empty public constructor
    }
    /**
     * @return A new instance of fragment SpeedDialFragment.
     */
    public static TwoFragment newInstance() {
        return new TwoFragment();
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

        View root = inflater.inflate(R.layout.fragment_two, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewState);
        final SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.swiperefreshState);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkConnected()) {
                            StateList= new ArrayList<>();
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
        //State tmp=new State("Tamil Nadu","10","10","10","2","1","1","Cases");
        //StateList.add(tmp);

        if (isNetworkConnected()) {
            new MyTask().execute(true);
        } else {
            Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();

        }
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
                    .url("https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise/history")
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
                    .url("https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise")
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
            StateList = new ArrayList<>();

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
               // System.out.println(result1[0]);
                JSONObject var1=new JSONObject(result1[0]);
                JSONObject TodayData=new JSONObject(result1[1]);
                JSONObject varB=TodayData.getJSONObject("data");



                JSONObject var2=var1.getJSONObject("data");

                JSONArray country= var2.getJSONArray("history");
               // System.out.println(country.length()-2);
                //System.out.println(country.length()-1);
                JSONArray sortedJsonArray = new JSONArray();
                list = new ArrayList();

                /* logic */
                Calendar temp= (Calendar) Calendar.getInstance();;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                temp.add(Calendar.DAY_OF_MONTH, -1);
                String newDate1 = sdf.format(temp.getTime());
                JSONObject varA;

                if(country.getJSONObject(country.length()-1).getString("day").equals(newDate1))
                {
                    varA=country.getJSONObject((country.length()-1) );
                }
                else
                {
                    varA= country.getJSONObject((country.length()-2) );
                }



               // JSONObject varB=country.getJSONObject(country.length()-1);

                //System.out.println(varA);
                //System.out.println(varB);
                JSONObject varA1=varA.getJSONObject("total");
                JSONObject varB1=varB.getJSONObject("total");

                JSONArray stateA=varB.getJSONArray("statewise");
                JSONArray stateB=varA.getJSONArray("statewise");

                confirmed=varB1.getString("confirmed");
                deaths=varB1.getString("deaths");
                recovered = varB1.getString("recovered");
                active = varB1.getString("active");


                StateList.add(0,new State("Total", confirmed, deaths, recovered, String.valueOf(Long.parseLong(confirmed)-Long.parseLong(varA1.getString("confirmed"))), String.valueOf(Long.parseLong(deaths)-Long.parseLong(varA1.getString("deaths"))), active,"Unsorted"));

               // StateList.add(0, new State("Total",varB.getString("confirmed"), varB.getString("deaths"), varB.getString("recovered"), todayCases, todayDeaths, active,"Unsorted"));


                for (int i = 0; i < stateA.length(); i++) {
                    list.add(stateA.getJSONObject(i));
                }


                for (int i = 0; i < list.size(); i++) {
                    //JSONObject lhs=(JSONObject)list.get(i);
                    for (int j = 0; j < list.size() - i - 1; j++) {
                        JSONObject lhs = (JSONObject) list.get(j);
                        JSONObject rhs = (JSONObject) list.get(j + 1);

                        if (Long.parseLong(lhs.getString("confirmed")) < Long.parseLong(rhs.getString("confirmed"))) {
                            JSONObject tmp;
                            tmp = (JSONObject) list.get(j);
                            list.set(j, rhs);
                            list.set(j + 1, tmp);

                        }
                    }
                }
                for (int i = 0; i < stateA.length(); i++) {
                    sortedJsonArray.put(list.get(i));
                }
                for (int i = 0; i < sortedJsonArray.length(); i++) {

                    JSONObject c = sortedJsonArray.getJSONObject(i);


                    confirmed = c.getString("confirmed");
                    deaths = c.getString("deaths");
                    recovered = c.getString("recovered");
                    cntryname = c.getString("state");
                    System.out.println("la"+cntryname);

                    for (int k = 0; k < stateB.length(); k++) {
                        System.out.println("lala"+stateB.getJSONObject(k).getString("state"));
                            String tmp=stateB.getJSONObject(k).getString("state");
                        if(tmp.equals(cntryname))
                        {

                            System.out.println(stateB.getJSONObject(k).getString("state"));
                            todayCases = String.valueOf(Long.parseLong(confirmed)- Long.parseLong(stateB.getJSONObject(k).getString("confirmed")));
                            todayDeaths = String.valueOf(Long.parseLong(deaths)- Long.parseLong(stateB.getJSONObject(k).getString("deaths")));
                        break;
                        }
                    }

                    /*todayCases = c.getString("todayCases");
                    todayDeaths = c.getString("todayDeaths");*/
                    active = c.getString("active");
                    //  System.out.println(confirmed + "   " + deaths + "    " + recovered);
                   /* if (cntryname.equals("total"))
                        StateList.add(0, new State("Total",varB.getString("confirmed"), varB.getString("deaths"), varB.getString("recovered"), todayCases, todayDeaths, active,"Unsorted"));
                    else*/
                        StateList.add(new State(cntryname, confirmed, deaths, recovered, todayCases, todayDeaths, active,"Cases"));
                    //  break;


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new StateAdapter(getActivity(), StateList, StateList);

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
                    menu.findItem(R.id.SortByDeaths).setVisible(false);
                    menu.findItem(R.id.SortByActiveCases).setVisible(false);
                    menu.findItem(R.id.SortByRecovered).setVisible(false);
                    menu.findItem(R.id.SortByNewCases).setVisible(false);
                    menu.findItem(R.id.SortByNewDeath).setVisible(false);


                    searchView.clearFocus();
                    if (isNetworkConnected() && adapter instanceof StateAdapter) {
                        adapter.getFilter().filter(query);
                    } else {
                        Toast.makeText(getActivity(), "Check Your Internet connection", Toast.LENGTH_SHORT).show();



                }


                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0) {

                    menu.findItem(R.id.SortByDeaths).setVisible(false);
                    menu.findItem(R.id.SortByActiveCases).setVisible(false);
                    menu.findItem(R.id.SortByRecovered).setVisible(false);
                    menu.findItem(R.id.SortByNewCases).setVisible(false);
                    menu.findItem(R.id.SortByNewDeath).setVisible(false);

                    if (isNetworkConnected() && adapter instanceof StateAdapter) {
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
                if (isNetworkConnected() && adapter instanceof StateAdapter) {
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
        if (isNetworkConnected() && adapter instanceof StateAdapter) {
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