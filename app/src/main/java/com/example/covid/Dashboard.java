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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Dashboard extends AppCompatActivity {

    BarChart barChart,barChart1,barChart2,barChart0;
    ProgressDialog p;
    ArrayList<Graph> dataAdapter;
    String res[];
    String YourtransferredData;
    String mindate="";
    String maxdate="";
    LinearLayout mainLayout;
    TextView DateRange,Con,Dea,Rec;
    PieChart pieChart;
    PieData pieData;
     TextView yValue,xValue,yValue1,xValue1,yValue2,xValue2,yValue0,xValue0,p1,p2,p3;
    PieDataSet pieDataSet;
    ArrayList<Entry> pieEntries;
    Double Pconf,PDea,Prec,Pact;
    int[] MY_COLORS = {
            Color.  rgb(255, 208, 140),
            Color. rgb(255, 140, 157),
            Color.rgb(192, 255, 140)
    };
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mainLayout=(LinearLayout)findViewById(R.id.Linear) ;
        mainLayout.setVisibility(LinearLayout.INVISIBLE);
        DateRange=(TextView) findViewById(R.id.textDateRange);
        Con=(TextView) findViewById(R.id.textConfirmedCountDashboard);
        Dea=(TextView) findViewById(R.id.textDeathCountDashboard);
        Rec=(TextView) findViewById(R.id.textRecoveredCountDashboard);

        pieChart = findViewById(R.id.pieChart);
        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);

        Intent intent = getIntent();
        YourtransferredData = intent.getExtras().getString("Mydata");
        res=YourtransferredData.split(",");
        //getSupportActionBar().setTitle(res[0].toUpperCase());
        TextView tv = new TextView(getApplicationContext());

        androidx.appcompat.app.ActionBar bar = getSupportActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(res[0].toUpperCase());
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        tv.setMaxLines(1);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(tv);
       // bar.setDisplayHomeAsUpEnabled(true);


        barChart0 = (BarChart) findViewById(R.id.barchart0);
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart1 = (BarChart) findViewById(R.id.barchart1);
        barChart2 = (BarChart) findViewById(R.id.barchart2);

        barChart0.setPinchZoom(true);
        barChart.setPinchZoom(true);
        barChart1.setPinchZoom(true);
        barChart2.setPinchZoom(true);

        barChart0.animateY(2000);
        barChart.animateY(2000);
        barChart1.animateY(2000);
        barChart2.animateY(2000);
        xValue0=(TextView)findViewById(R.id.XValue0);

        yValue0=(TextView)findViewById(R.id.YValue0);

        xValue=(TextView)findViewById(R.id.XValue);

         yValue=(TextView)findViewById(R.id.YValue);

        xValue1=(TextView)findViewById(R.id.XValue1);

        yValue1=(TextView)findViewById(R.id.YValue1);

        xValue2=(TextView)findViewById(R.id.XValue2);

        yValue2=(TextView)findViewById(R.id.YValue2);


        p1=(TextView)findViewById(R.id.XValueP);

        p2=(TextView)findViewById(R.id.YValueP);

        p3=(TextView)findViewById(R.id.ZValueP);


        XAxis xAxis0 = barChart0.getXAxis();
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight0 = barChart0.getAxisRight();
        yAxisRight0.setEnabled(false);
        YAxis yAxisleft0 = barChart0.getAxisLeft();
        yAxisleft0.setValueFormatter(new MyYAxisValueFormatter("M"));
        barChart0.getAxisLeft().setDrawGridLines(false);
        barChart0.getXAxis().setDrawGridLines(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisleft = barChart.getAxisLeft();
        yAxisleft.setValueFormatter(new MyYAxisValueFormatter("M"));
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);

        XAxis xAxis1 = barChart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight1 = barChart1.getAxisRight();
        yAxisRight1.setEnabled(false);
        YAxis yAxisleft1 = barChart1.getAxisLeft();
        yAxisleft1.setValueFormatter(new MyYAxisValueFormatter("M"));
        barChart1.getAxisLeft().setDrawGridLines(false);
        barChart1.getXAxis().setDrawGridLines(false);

        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight2 = barChart2.getAxisRight();
        yAxisRight2.setEnabled(false);
        YAxis yAxisleft2 = barChart2.getAxisLeft();
        yAxisleft2.setValueFormatter(new MyYAxisValueFormatter("M"));
        barChart2.getAxisLeft().setDrawGridLines(false);
        barChart2.getXAxis().setDrawGridLines(false);

        if (isNetworkConnected()) {
            new MyTask().execute(true);

        } else {
            Toast.makeText(Dashboard.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();

        }



        barChart0.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {


            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                final String selectedValue=barChart0.getXAxis().getValues().get(e.getXIndex());
                Log.d("selctdX", selectedValue);

                //YValue
                final String selectedYValue = String.valueOf(String.format("%.0f",e.getVal()));
                Log.d("selctdY", selectedYValue);
                xValue0.setText("X Value: "+selectedValue);
                yValue0.setText("Y Value: "+selectedYValue);
            }
            @Override
            public void onNothingSelected()
            {
                xValue0.setText("X Value: NA");
                yValue0.setText("Y Value: NA");
            }
        });
       barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {


            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                final String selectedValue=barChart.getXAxis().getValues().get(e.getXIndex());
                Log.d("selctdX", selectedValue);

                //YValue
                final String selectedYValue = String.valueOf(String.format("%.0f",e.getVal()));
                Log.d("selctdY", selectedYValue);
                xValue.setText("X Value: "+selectedValue);
                yValue.setText("Y Value: "+selectedYValue);
            }

            @Override
            public void onNothingSelected()
            {
                xValue.setText("X Value: NA");
                yValue.setText("Y Value: NA");
            }
        });
        barChart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {


            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                final String selectedValue=barChart1.getXAxis().getValues().get(e.getXIndex());
                Log.d("selctdX", selectedValue);

                //YValue
                final String selectedYValue = String.valueOf(String.format("%.0f",e.getVal()));
                Log.d("selctdY", selectedYValue);
                xValue1.setText("X Value: "+selectedValue);
                yValue1.setText("Y Value: "+selectedYValue);
            }

            @Override
            public void onNothingSelected()
            {
                xValue1.setText("X Value: NA");
                yValue1.setText("Y Value: NA");
            }
        });

        barChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {


            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                final String selectedValue=barChart2.getXAxis().getValues().get(e.getXIndex());
                Log.d("selctdX", selectedValue);

                //YValue
                final String selectedYValue = String.valueOf(String.format("%.0f",e.getVal()));
                Log.d("selctdY", selectedYValue);
                xValue2.setText("X Value: "+selectedValue);
                yValue2.setText("Y Value: "+selectedYValue);
            }

            @Override
            public void onNothingSelected()
            {
                xValue2.setText("X Value: NA");
                yValue2.setText("Y Value: NA");
            }
        });


        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                Toast.makeText(Dashboard.this, "Double", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
               // Toast.makeText(Dashboard.this, "Single", Toast.LENGTH_SHORT).show();
                p1.setText("Active   : "+String.format("%.0f",Pact)+" ("+String.format("%.2f",(Pact/Pconf)*100)+"%)");
                p2.setText("Death    : "+String.format("%.0f",PDea)+" ("+String.format("%.2f",(PDea/Pconf)*100)+"%)" );
                p3.setText("Recovered: "+String.format("%.0f",Prec)+" ("+String.format("%.2f",(Prec/Pconf)*100)+"%)");


            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }

        });

    }

    private class MyTask extends AsyncTask<Boolean, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        p = new ProgressDialog(Dashboard.this);
        p.setMessage("Loading...");
        p.setIndeterminate(false);
        p.setCancelable(false);
            p.show();



        }

        @Override
        protected String doInBackground(Boolean... a) {

            String ActualResult = "";
            System.out.println("SSaaa1");
if(res[0].equals("USA"))
    res[0]="US";
else if(res[0].equals("UAE"))
res[0]="United Arab Emirates";
    else if(res[0].equals("UK"))
        res[0]="United Kingdom";

    if(res[0].equals("India") || res.length==2)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
                .url("https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise/history")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ActualResult += response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    else {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.coronastatistics.live/timeline/" + res[0])
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ActualResult += response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

            return ActualResult;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                //System.out.println(res[0]);

                System.out.println("result is"+result);
                if(res[0].equals("GLOBAL"))
                {
                    JSONObject newOb1 = new JSONObject(result);
                    Iterator<String> keys = newOb1.keys();
                    dataAdapter = new ArrayList<>();
                    String key="";

                    while(keys.hasNext()) {
                        key=keys.next();
                        System.out.println("key"+ key);

                        String dateFormat[] = key.split("-");
                        dateFormat[1] = "00" + dateFormat[1];
                        dateFormat[2] = "00" + dateFormat[2];
                        JSONObject tmp=(JSONObject) newOb1.get(key);



                        dataAdapter.add(new Graph(Long.parseLong(tmp.getString("cases")),Long.parseLong(tmp.getString("deaths")),Long.parseLong(tmp.getString("recovered")),
                                dateFormat[0] + "-" + dateFormat[1].substring(dateFormat[1].length() - 2, dateFormat[1].length()) + "-" + dateFormat[2].substring(dateFormat[2].length() - 2, dateFormat[2].length())));
                    }
                    maxdate = dataAdapter.get(dataAdapter.size() - 2).getDate();
                    mindate = dataAdapter.get(0).getDate();

                }
                else if(res[0].equals("India"))
                {
                    JSONObject newOb = new JSONObject(result);
                    JSONObject newOb1 = newOb.getJSONObject("data");
                    JSONArray timeline = newOb1.getJSONArray("history");
                    dataAdapter = new ArrayList<>();
                    for (int i = 0; i < timeline.length() ; i++) {

                        if(i!=0 && !(timeline.getJSONObject(i).getString("day").equals(timeline.getJSONObject(i-1).getString("day")))) {
                            dataAdapter.add(new Graph(Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("confirmed")),

                                    Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("deaths")),
                                    Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("recovered")),
                                    timeline.getJSONObject(i).getString("day")
                            ));
                        }
                        else if(i==0)
                        {
                            dataAdapter.add(new Graph(Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("confirmed")),

                                    Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("deaths")),
                                    Long.parseLong(timeline.getJSONObject(i).getJSONObject("total").getString("recovered")),
                                    timeline.getJSONObject(i).getString("day")
                            ));
                        }

                    }


                    maxdate = dataAdapter.get(dataAdapter.size()-1).getDate();
                    mindate = dataAdapter.get(0).getDate();




                }
                else if(res.length==2)
                {
                    JSONObject newOb = new JSONObject(result);
                    JSONObject newOb1 = newOb.getJSONObject("data");
                    JSONArray timeline = newOb1.getJSONArray("history");
                    dataAdapter = new ArrayList<>();
                    for (int i = 0; i < timeline.length() ; i++) {
                        JSONArray timeline1 = timeline.getJSONObject(i).getJSONArray("statewise");

                        for(int j=0;j<timeline1.length();j++)
                        {
                            if(timeline1.getJSONObject(j).getString("state").equals(res[0]))
                            {
                                dataAdapter.add(new Graph(Long.parseLong(timeline1.getJSONObject(j).getString("confirmed")),

                                        Long.parseLong(timeline1.getJSONObject(j).getString("deaths")),
                                        Long.parseLong(timeline1.getJSONObject(j).getString("recovered")),
                                        timeline.getJSONObject(i).getString("day")
                                ));

                                break;
                            }
                        }
                    }
                    maxdate = dataAdapter.get(dataAdapter.size()-1).getDate();
                    mindate = dataAdapter.get(0).getDate();


                }
                else {

                    JSONObject newOb = new JSONObject(result);

                    JSONObject newOb1 = newOb.getJSONObject("data");

                    JSONArray timeline = newOb1.getJSONArray("timeline");
                    // timelineCnt=timeline.length();
                   // Graph Gdata;
                    dataAdapter = new ArrayList<>();
                    for (int i = 0; i < timeline.length() ; i++) {
                        //  System.out.println("SSaaa"+i+timeline.get(i));
                        String dateFormat[] = timeline.getJSONObject(i).getString("date").split("-");
                        dateFormat[1] = "00" + dateFormat[1];
                        dateFormat[2] = "00" + dateFormat[2];

                        dataAdapter.add(new Graph(Long.parseLong(timeline.getJSONObject(i).getString("cases")), Long.parseLong(timeline.getJSONObject(i).getString("deaths")), Long.parseLong(timeline.getJSONObject(i).getString("recovered")),
                                dateFormat[0] + "-" + dateFormat[1].substring(dateFormat[1].length() - 2, dateFormat[1].length()) + "-" + dateFormat[2].substring(dateFormat[2].length() - 2, dateFormat[2].length())));

                    }
                    maxdate = dataAdapter.get(dataAdapter.size()-1).getDate();
                    mindate = dataAdapter.get(0).getDate();

                }

                //System.out.println("Dateeee"+new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
             //  dataAdapter.add(new Graph(Long.parseLong(res[1]),Long.parseLong(res[2]),Long.parseLong(res[3]),(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())).toString()));
                ArrayList<BarEntry>   entries0 = new ArrayList<>();
                BarDataSet bardataset0 = new BarDataSet(entries0, "Active Cases");
                ArrayList<BarEntry>   entries = new ArrayList<>();
                BarDataSet bardataset = new BarDataSet(entries, "Confirmed Cases");
                ArrayList<BarEntry> entries1 = new ArrayList<>();
                BarDataSet bardataset1 = new BarDataSet(entries1, "Deaths");
                ArrayList<BarEntry> entries2 = new ArrayList<>();
                BarDataSet bardataset2 = new BarDataSet(entries2, "Recovered");
                ArrayList<String> labels0 = new ArrayList<String>();

                ArrayList<String> labels = new ArrayList<String>();
                ArrayList<String> labels1 = new ArrayList<String>();
                ArrayList<String> labels2 = new ArrayList<String>();


                int k=0;
                for (int i = dataAdapter.size()-6; i < dataAdapter.size(); i++) {
                    System.out.println("SSaaa" + i + dataAdapter.get(i).getDate());
                    String dateFormat1[]=dataAdapter.get(i).getDate().split("-");
                    entries.add(new BarEntry(dataAdapter.get(i).getConfirmed(), k));
                    labels.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                    entries1.add(new BarEntry(dataAdapter.get(i).getDeath(), k));
                    labels1.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                    entries2.add(new BarEntry(dataAdapter.get(i).getRecovered(), k));
                    labels2.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                    entries0.add(new BarEntry(dataAdapter.get(i).getConfirmed()-(dataAdapter.get(i).getDeath()+dataAdapter.get(i).getRecovered()), k++));
                    labels0.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));

                }
//System.out.println("Check"+dataAdapter.get(dataAdapter.size()-1).getConfirmed() +" "+dataAdapter.get(dataAdapter.size()-1).getDate());


                 Pconf=Double.parseDouble(dataAdapter.get(dataAdapter.size()-1).getConfirmed().toString());
                 PDea=Double.parseDouble(dataAdapter.get(dataAdapter.size()-1).getDeath().toString());
                 Prec=Double.parseDouble(dataAdapter.get(dataAdapter.size()-1).getRecovered().toString());
                 Pact=Pconf-(PDea+Prec);




                pieEntries = new ArrayList<>();
                int inc=0;
                pieEntries.add(new Entry((float)(Pact/Pconf)*100, inc++));
                pieEntries.add(new Entry((float)(PDea/Pconf)*100, inc++));
                pieEntries.add(new Entry((float)(Prec/Pconf)*100, inc++));


                ArrayList<String> labelsP = new ArrayList<String>();
                labelsP.add("");
                labelsP.add("");
                labelsP.add("");

                pieChart.setMinimumWidth(50);
                pieDataSet = new PieDataSet(pieEntries, "Active, Death, Recovered % on End date");
              //  pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieChart.setDescription("");
                pieData = new PieData(labelsP,pieDataSet);
                pieChart.setData(pieData);

                pieData.setValueFormatter(new PercentFormatter());
                if(((Prec/Pconf)*100<5f )||((PDea/Pconf)*100<5f) || ((Pact/Pconf)*100< 5f) )
                {
                    pieChart.getData().setDrawValues(false) ;

                }

                ArrayList<Integer> colors = new ArrayList<>();
                for(int c: MY_COLORS) colors.add(c);

                pieDataSet.setColors(colors);
                //pieDataSet.setSliceSpace(5f);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieChart.setDrawSliceText(false);


                pieDataSet.setValueTextSize(8f);
                pieChart.animateY(2000);

                BarData data0 = new BarData(labels0, bardataset0);
                barChart0.setData(data0); // set the data and list of labels into chart
                barChart0.setDescription("");  // set the description
                bardataset0.setColors(ColorTemplate.VORDIPLOM_COLORS);
                bardataset0.setHighlightEnabled(true); // allow highlighting for DataSet
                data0.setGroupSpace(0.9f);

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data); // set the data and list of labels into chart
                barChart.setDescription("");  // set the description
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                bardataset.setHighlightEnabled(true); // allow highlighting for DataSet
                data.setGroupSpace(0.9f);

                BarData data1 = new BarData(labels1, bardataset1);
                barChart1.setData(data1); // set the data and list of labels into chart
                barChart1.setDescription("");  // set the description
                bardataset1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                bardataset1.setHighlightEnabled(true); // allow highlighting for DataSet
                data1.setGroupSpace(0.9f);

                BarData data2 = new BarData(labels2, bardataset2);
                barChart2.setData(data2); // set the data and list of labels into chart
                barChart2.setDescription("");  // set the description
                bardataset2.setColors(ColorTemplate.VORDIPLOM_COLORS);
                bardataset2.setHighlightEnabled(true); // allow highlighting for DataSet
                data1.setGroupSpace(0.9f);

                DateRange.setText(mindate+" - "+maxdate);
                Con.setText(dataAdapter.get(dataAdapter.size()-1).getConfirmed().toString());
                Dea.setText(dataAdapter.get(dataAdapter.size()-1).getDeath().toString());

                Rec.setText(dataAdapter.get(dataAdapter.size()-1).getRecovered().toString());

                p.hide();
                mainLayout.setVisibility(LinearLayout.VISIBLE);
            } catch (JSONException e) {


                e.printStackTrace();
                Toast.makeText(Dashboard.this, "Historic data not available", Toast.LENGTH_SHORT).show();

                finish();

            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            Calendar cal = (Calendar) Calendar.getInstance();
            Calendar cal1 = (Calendar) Calendar.getInstance();

            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(mindate));
                cal1.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(maxdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }



            SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                @Override
                public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                           int yearStart, int monthStart,
                                           int dayStart, int yearEnd,
                                           int monthEnd, int dayEnd) {
                   // smoothDateRangePickerFragment.setMaxDate(cal1);

                    // Toast.makeText(Dashboard.this, "Dashhh" + monthStart + "-" + dayStart + "-" + yearStart, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(Dashboard.this, "Dashhh" + monthEnd + "-" + dayEnd + "-" + yearEnd, Toast.LENGTH_SHORT).show();

                    String month="00"+(monthStart+1);
                    String day="00"+(dayStart);
                    String stDate=yearStart+"-"+month.substring(month.length()-2,month.length())+"-"+day.substring(day.length()-2,day.length());

                    month="00"+(monthEnd+1);
                    day="00"+(dayEnd);
                    String edDate=yearEnd+"-"+month.substring(month.length()-2,month.length())+"-"+day.substring(day.length()-2,day.length());
                    try {
                        if(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(edDate).after(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(maxdate))) {
                          //  Toast.makeText(Dashboard.this, "Dashhh" + edDate, Toast.LENGTH_SHORT).show();

                            Toast.makeText(Dashboard.this, "Please don't select greyed out dates", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ArrayList<BarEntry>   entries0 = new ArrayList<>();
                            BarDataSet bardataset0 = new BarDataSet(entries0, "Active Cases");
                            ArrayList<BarEntry>   entries = new ArrayList<>();
                            BarDataSet bardataset = new BarDataSet(entries, "Confirmed Cases");
                            ArrayList<BarEntry> entries1 = new ArrayList<>();
                            BarDataSet bardataset1 = new BarDataSet(entries1, "Deaths");
                            ArrayList<BarEntry> entries2 = new ArrayList<>();
                            BarDataSet bardataset2 = new BarDataSet(entries2, "Recovered");
                            ArrayList<String> labels0 = new ArrayList<String>();
                            ArrayList<String> labels = new ArrayList<String>();
                            ArrayList<String> labels1 = new ArrayList<String>();
                            ArrayList<String> labels2 = new ArrayList<String>();
                            int k=0;
                            // int inc = (timelineCnt - 2) / 6;

                            ArrayList<Graph> dataAdaptertemp=new ArrayList<>();
                            int flag=0;
                            Long stcon=0L;
                                    Long stdea=0L;
                            Long strec=0L;
                            for (int i = 0; i < dataAdapter.size(); i++) {
                                try {
                                    Calendar temp= (Calendar) Calendar.getInstance();;
                                   // Date End = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(edDate);
                                    temp.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(edDate));
                                    temp.add(Calendar.DAY_OF_MONTH, 1);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                    String newDate = sdf.format(temp.getTime());

                                    temp.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(stDate));

                                    temp.add(Calendar.DAY_OF_MONTH, -1);
                                    String newDate1 = sdf.format(temp.getTime());

                                   // Toast.makeText(Dashboard.this, "Old"+newDate+" New"+newDate1, Toast.LENGTH_SHORT).show();
//System.out.println("Old"+newDate1+" New"+newDate);

                                    if (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataAdapter.get(i).getDate()).after(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(newDate1))
                                    && new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataAdapter.get(i).getDate()).before(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(newDate)))


                                    {
                                        if((i!=0 ) && (flag==0))
                                        {
                                            stcon=dataAdapter.get(i-1).getConfirmed();
                                                    stdea=dataAdapter.get(i-1).getDeath();
                                                    strec=dataAdapter.get(i-1).getRecovered();
                                        }

                                        dataAdaptertemp.add(dataAdapter.get(i));
                                        flag=1;



                                    } else if(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataAdapter.get(i).getDate()).after(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(newDate))){
                                        break;
                                    }
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();

                                }
                            }
                             int inc = dataAdaptertemp.size() / 6;
                            if(inc==0)
                                inc=1;

                                for (int i = 0; i < dataAdaptertemp.size()&&k<6; i=i+inc) {
                                   // Toast.makeText(Dashboard.this, "Data"+dataAdaptertemp.get(i).getDate(), Toast.LENGTH_SHORT).show();

                                    //System.out.println("SSaaa" + i + timeline.get(i));
                                String dateFormat1[]=dataAdaptertemp.get(i).getDate().split("-");
                                entries.add(new BarEntry(dataAdaptertemp.get(i).getConfirmed(), k));
                                labels.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                entries1.add(new BarEntry(dataAdaptertemp.get(i).getDeath(), k));
                                labels1.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                entries2.add(new BarEntry(dataAdaptertemp.get(i).getRecovered(), k));
                                labels2.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                    entries0.add(new BarEntry(dataAdaptertemp.get(i).getConfirmed()-(dataAdaptertemp.get(i).getDeath()+dataAdaptertemp.get(i).getRecovered()), k++));
                                    labels0.add(dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));


                                }
                            DateRange.setText(stDate+" - "+edDate);
                                Con.setText(String.valueOf(dataAdaptertemp.get(dataAdaptertemp.size()-1).getConfirmed()-stcon));
                            Dea.setText(String.valueOf(dataAdaptertemp.get(dataAdaptertemp.size()-1).getDeath()-stdea));

                            Rec.setText(String.valueOf(dataAdaptertemp.get(dataAdaptertemp.size()-1).getRecovered()-strec));

                            if(k==6)
                                {
                                entries.set(5,(new BarEntry(dataAdaptertemp.get(dataAdaptertemp.size()-1).getConfirmed(), 5)));
                                    entries1.set(5,(new BarEntry(dataAdaptertemp.get(dataAdaptertemp.size()-1).getDeath(), 5)));
                                    entries2.set(5,(new BarEntry(dataAdaptertemp.get(dataAdaptertemp.size()-1).getRecovered(), 5)));
                                    entries0.set(5,(new BarEntry(dataAdaptertemp.get(dataAdaptertemp.size()-1).getConfirmed()-(dataAdaptertemp.get(dataAdaptertemp.size()-1).getDeath()+dataAdaptertemp.get(dataAdaptertemp.size()-1).getRecovered()), 5)));



                                    String dateFormat1[]=dataAdaptertemp.get(dataAdaptertemp.size()-1).getDate().split("-");
                            labels.set(5,dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                    labels1.set(5,dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                    labels2.set(5,dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));
                                    labels0.set(5,dateFormat1[2]+"-"+dateFormat1[1]+"-"+dateFormat1[0].substring(dateFormat1[0].length()-2,dateFormat1[0].length()));


                                }

                             Pconf=Double.parseDouble(dataAdaptertemp.get(dataAdaptertemp.size()-1).getConfirmed().toString());
                             PDea=Double.parseDouble(dataAdaptertemp.get(dataAdaptertemp.size()-1).getDeath().toString());
                             Prec=Double.parseDouble(dataAdaptertemp.get(dataAdaptertemp.size()-1).getRecovered().toString());
                             Pact=Pconf-(PDea+Prec);
                            p1.setText("Active   : "+"NA");
                            p2.setText("Death    : "+"NA");
                            p3.setText("Recovered: "+"NA");
                            pieEntries = new ArrayList<>();
                            int inc1=0;
                                pieEntries.add(new Entry((float)(Pact/Pconf)*100, inc1++));
                                pieEntries.add(new Entry((float)(PDea/Pconf)*100, inc1++));
                                pieEntries.add(new Entry((float)(Prec/Pconf)*100, inc1++));


                            pieDataSet = new PieDataSet(pieEntries, "Active, Death, Recovered % on End date");
                            ArrayList<String> labelsP = new ArrayList<String>();
                            labelsP.add("");
                            labelsP.add("");
                            labelsP.add("");

                            pieData = new PieData(labelsP,pieDataSet);
                            pieChart.setData(pieData);
                            pieData.setValueFormatter(new PercentFormatter());

                            ArrayList<Integer> colors = new ArrayList<>();
                            for(int c: MY_COLORS) colors.add(c);

                            pieDataSet.setColors(colors);
                           // pieDataSet.setSliceSpace(5f);
                            pieDataSet.setValueTextColor(Color.BLACK);
                            pieChart.setDrawSliceText(false);
                            if(((Prec/Pconf)*100<5f )||((PDea/Pconf)*100<5f) || ((Pact/Pconf)*100< 5f) )
                            {
                                pieChart.getData().setDrawValues(false) ;

                            }
                            pieDataSet.setValueTextSize(8f);
                            pieChart.animateY(2000);
                            pieDataSet.notifyDataSetChanged();
                            pieChart.invalidate();


                            xValue0.setText("X Value: NA");
                            yValue0.setText("Y Value: NA");
                            xValue.setText("X Value: NA");
                            yValue.setText("Y Value: NA");
                            xValue1.setText("X Value: NA");
                            yValue1.setText("Y Value: NA");
                            xValue2.setText("X Value: NA");
                            yValue2.setText("Y Value: NA");

                            barChart0.animateY(1000);
                            barChart.animateY(1000);
                            barChart1.animateY(1000);
                            barChart2.animateY(1000);

                            BarData data0 = new BarData(labels0, bardataset0);
                            barChart0.setData(data0); // set the data and list of labels into chart
                            barChart0.setDescription("");  // set the description
                            bardataset0.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            bardataset0.setHighlightEnabled(true); // allow highlighting for DataSet
                            data0.setGroupSpace(0.9f);
                            bardataset0.notifyDataSetChanged();
                            barChart0.invalidate();

                            BarData data = new BarData(labels, bardataset);
                            barChart.setData(data); // set the data and list of labels into chart
                            barChart.setDescription("");  // set the description
                            bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            bardataset.setHighlightEnabled(true); // allow highlighting for DataSet
                            data.setGroupSpace(0.9f);
                            bardataset.notifyDataSetChanged();
                            barChart.invalidate();

                            BarData data1 = new BarData(labels1, bardataset1);
                            barChart1.setData(data1); // set the data and list of labels into chart
                            barChart1.setDescription("");  // set the description
                            bardataset1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            bardataset1.setHighlightEnabled(true); // allow highlighting for DataSet
                            data1.setGroupSpace(0.9f);
                            bardataset1.notifyDataSetChanged();
                            barChart1.invalidate();

                            BarData data2 = new BarData(labels2, bardataset2);
                            barChart2.setData(data2); // set the data and list of labels into chart
                            barChart2.setDescription("");  // set the description
                            bardataset2.setColors(ColorTemplate.VORDIPLOM_COLORS);
                            bardataset2.setHighlightEnabled(true); // allow highlighting for DataSet
                            data1.setGroupSpace(0.9f);
                            bardataset2.notifyDataSetChanged();
                            barChart2.invalidate();



                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //System.out.print("Dashhh" + monthStart + "-" + dayStart + "-" + yearStart);
                }
            });
            smoothDateRangePickerFragment.show(getFragmentManager(), "show Date Range");
            smoothDateRangePickerFragment.setMinDate(cal);
           smoothDateRangePickerFragment.setMaxDate(cal1);
            //smoothDateRangePickerFragment.setAccentColor(R.color.colorPrimary);
            // smoothDateRangePickerFragment.setThemeDark(true);

            //smoothDateRangePickerFragment.setThemeDark(true);


        }
        return true;
    }
    private boolean isNetworkConnected () {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}




