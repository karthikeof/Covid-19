package com.example.covid;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

class MyXAxisValueFormatter implements YAxisValueFormatter
{
    public MyXAxisValueFormatter(String m) {
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        if(value<1000)
        {
            return String.valueOf(value);
        }

        else if(value>=1000 & value<1000000)
        {
            return String.valueOf(value/1000)+"K";

        }
        else {
            return String.valueOf(value / 1000000) + "M";

        }
    }
}
