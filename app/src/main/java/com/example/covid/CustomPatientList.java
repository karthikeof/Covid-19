package com.example.covid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.List;

class CustomPatientList extends ArrayAdapter {

    private Integer[] imageid;
    private Activity context;
    List<Patient> PatientList;

    public CustomPatientList(Activity context, List<Patient> PatientList, Integer[] imageid) {
        super(context, R.layout.layout_patient_list, PatientList);
        this.context = context;
        this.PatientList = PatientList;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.layout_patient_list, null, true);
        TextView PatientNumber = (TextView) row.findViewById(R.id.textViewPatientNumber);
        TextView Notes = (TextView) row.findViewById(R.id.textViewNotes);
        TextView Date = (TextView) row.findViewById(R.id.Date);
        TextView status=(TextView) row.findViewById(R.id.Status);

        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageView);
        final Patient patient = PatientList.get(position);

        PatientNumber.setText(String.valueOf("P"+patient.getPatientNum())+"-"+(patient.getAge().isEmpty()?"NA":patient.getAge())+" ");//+" "+(patient.getgender().isEmpty()?"NA":patient.getgender())));
        Notes.setText(patient.getNotes());
        //Age.setText("AGE: "+(patient.getAge().isEmpty()?"NA":patient.getAge())+" ");
        Date.setText(patient.getdate()+" ");


        if(patient.getgender().equals("M"))
            imageFlag.setImageResource(imageid[0]);
        else if(patient.getgender().equals("F"))
            imageFlag.setImageResource(imageid[1]);
        else
            imageFlag.setImageResource(imageid[2]);

        if(patient.getstatus().equals("Recovered"))
        {
            status.setText("R");
            status.setBackgroundResource(R.drawable.circle_r);
            status.setTextColor( ContextCompat.getColor(context, R.color.textColorPrimary));

        }
        else if(patient.getstatus().equals("Hospitalized"))
        {
            status.setText("H");
            status.setBackgroundResource(R.drawable.circle);
            status.setTextColor( ContextCompat.getColor(context, R.color.TextColour));
        }
            else if(patient.getstatus().equals("Deceased"))
        {
            status.setText("D");
            status.setBackgroundResource(R.drawable.circle_d);
            status.setTextColor( ContextCompat.getColor(context, R.color.textColorPrimary));

        }

        return  row;
    }
}