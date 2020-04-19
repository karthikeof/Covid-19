package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class  PatientList extends AppCompatActivity implements AbsListView.OnScrollListener {

    String YourtransferredData[];

    private Integer imageid[] = {
            R.drawable.male,
            R.drawable.female,
            R.drawable.icons
    };
    private ListView listView;
    List<Patient> PatientList;
    CustomPatientList customPatientList;
    ProgressDialog p;
    int threshold=0;
    int currentPage=-1;
    int flag=0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
         listView=(ListView)findViewById(R.id.listview);

        Intent intent = getIntent();
        YourtransferredData = intent.getExtras().getString("District").split(",");
       // getSupportActionBar().setTitle(YourtransferredData[0].toUpperCase());
        TextView tv = new TextView(getApplicationContext());

        androidx.appcompat.app.ActionBar bar = getSupportActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(YourtransferredData[0].toUpperCase());
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        tv.setMaxLines(1);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(tv);


        PatientList = new ArrayList<>();
        if(isNetworkConnected()) {
            new MyTask().execute(true);

            customPatientList = new CustomPatientList(PatientList.this, PatientList, imageid);

            listView.setAdapter(customPatientList);
            // if(flag==0) {
        }
            else {
            Toast.makeText(getApplicationContext(), "No Internet connection/ No data", Toast.LENGTH_SHORT).show();

        }


        // For populating list data
       // CustomCountryList customCountryList = new CustomCountryList(this, countryNames, capitalNames, imageid);
      //  listView.setAdapter(customCountryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(),"You Selected "+countryNames[position-1]+ " as Country",Toast.LENGTH_SHORT).show();        }
            }
            });

           listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            System.out.println("Scroll state :"+listView.getLastVisiblePosition());

            if (listView.getLastVisiblePosition() >= listView.getCount() - 1 - threshold) {
               // currentPage;
                //load more list items:
               // Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                int position = listView.getSelectedItemPosition();
                if(currentPage!=0)
                new MyTask().execute(true);
                listView.setSelection(position);

            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    private class MyTask extends AsyncTask<Boolean, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(PatientList.this);
            p.setMessage("Loading...");
            p.setIndeterminate(false);
            p.setCancelable(false);
                p.show();


        }

        @Override
        protected String doInBackground(Boolean... a) {

            System.out.println("Hello");
            String ActualResult = "";
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://api.covid19india.org/raw_data.json")
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ActualResult += response.body().string();
              //  ActualResult += "#";

            } catch (IOException e) {
                e.printStackTrace();
            }


            return ActualResult;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject newOb=new JSONObject(result);
                JSONArray country = newOb.getJSONArray("raw_data");
                if(flag==0)
                {
                  //  System.out.println("Empty"+i);

                    currentPage=country.length()-1;
                   flag=1;
                }
 int cnt=1;
                int f=0;
                for (int i = currentPage; i > 0; i--) {

                    JSONObject tmp =country.getJSONObject(i);
                  // String districtname=tmp.getString("detecteddistrict");
                   if(YourtransferredData[0].equals("Unknown"))
                       YourtransferredData[0]="";

                  System.out.println("Empty"+i);
                    if(tmp.getString("detecteddistrict").trim().equals(YourtransferredData[0]) &&  tmp.getString("detectedstate").trim().equals(YourtransferredData[1]))
                    {
                        //String status=tmp.getString("currentstatus");

                       // System.out.println("Hello"+tmp.getString("patientnumber"));
                        PatientList.add(new Patient(Long.parseLong(tmp.getString("patientnumber")),tmp.getString("agebracket"),tmp.getString("notes"),tmp.getString("detecteddistrict"),tmp.getString("gender"),tmp.getString("currentstatus"),
                                 tmp.getString("statuschangedate").replace('/','-')));

                    if(cnt++>20)
                    {
                        currentPage=--i;
                        f=1;
                        break;
                    }

                    }
                }
                if(f==0)
                currentPage=0;



            } catch (JSONException e) {
                e.printStackTrace();
            }
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Format the date to Strings


            for(int i=0;i<PatientList.size();i++)
            {
                 for(int j=0;j<PatientList.size()-i-1;j++)
                 {
                     //System.out.println("Helloaaaa"+PatientList.get(j).getdate() +" "+PatientList.get(j+1).getdate());

                     try {
                         if((mdyFormat.parse(PatientList.get(j).getdate())).before(mdyFormat.parse(PatientList.get(j+1).getdate())))
                         {

                           //  System.out.println("Helloaaaa1"+PatientList.get(j).getdate() +" "+PatientList.get(j+1).getdate());

                             Patient temp;
                             temp=PatientList.get(j);
                             PatientList.set(j,PatientList.get(j+1));
                             PatientList.set(j+1,temp);

                         }

                         else if((mdyFormat.parse(PatientList.get(j).getdate())).equals(mdyFormat.parse(PatientList.get(j+1).getdate())))
                         {
                             if(PatientList.get(j).getPatientNum()<PatientList.get(j+1).getPatientNum())
                             {

                                 Patient temp;
                                 temp=PatientList.get(j);
                                 PatientList.set(j,PatientList.get(j+1));
                                 PatientList.set(j+1,temp);


                             }
                         }
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }

                 }

            }



            customPatientList.notifyDataSetChanged();


           p.hide();


        }
    }

    private boolean isNetworkConnected () {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
