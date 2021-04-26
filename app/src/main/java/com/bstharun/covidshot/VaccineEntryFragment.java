package com.bstharun.covidshot;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class VaccineEntryFragment extends Fragment {

    String[] vaccines = {
            "Covaxin",
            "Covishield",
            "Johnson & Johnson",
            "Moderna",
            "Novavax",
            "Oxford–AstraZeneca",
            "Pfizer-BioNTech",
            "Sputnik V"};

    String[] doses = {"Dose 1", "Dose 2", "Single Dose Vaccine"};

    TextView txtDate;
    Spinner spinnerVaccine;
    Spinner spinnerDose;

    public VaccineEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vaccine_entry, container, false);

        spinnerVaccine = root.findViewById(R.id.spinnerVaccineType);
        spinnerDose = root.findViewById(R.id.spinnerVaccineDose);
        txtDate = root.findViewById(R.id.txtDate);

        txtDate.setText(DateHelper.TodayToString());

        setVaccineSpinner();
        setVaccineDoseSpinner();


        txtDate.setOnClickListener(v -> {
            showCalendar();
        });


        return root;
    }



    void showCalendar(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        Date d = calendar.getTime();

                        txtDate.setText(DateHelper.DateToString(d));

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    void setVaccineSpinner() {


        spinnerVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("MyLog", "Selected " + vaccines[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter ad = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, vaccines);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinnerVaccine.setAdapter(ad);

    }

    void setVaccineDoseSpinner() {


        spinnerDose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("MyLog", "Selected " + doses[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter ad = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, doses);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinnerDose.setAdapter(ad);

    }

}