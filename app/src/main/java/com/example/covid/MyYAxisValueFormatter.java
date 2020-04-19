package com.example.covid;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

class MyYAxisValueFormatter implements YAxisValueFormatter
{
    public MyYAxisValueFormatter(String m) {
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        if(value<1000)
        {

            return String.valueOf(String.format("%.0f", value));
        }

        else if(value>=1000 & value<1000000)

        {
                return String.format("%.1f",(value/1000))+"K";



        }
        else {
                return String.format("%.1f",(value / 1000000)) + "M";


        }
    }
}
