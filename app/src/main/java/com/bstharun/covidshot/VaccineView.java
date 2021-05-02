package com.bstharun.covidshot;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VaccineView extends Fragment {

    TextView txtName;
    TextView txtDate;
    TextView txtVaccineType;
    TextView txtVaccineDose;
    TextView txtFrontImage;
    TextView txtBackImage;

    Vaccination vaccineRecord;
    FloatingActionButton btnHome;
    FloatingActionButton btnEdit;
    FloatingActionButton btnDelete;

    LinearLayout lvCameraFront;
    LinearLayout lvCameraBack;

    public VaccineView() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vaccine_view, container, false);

        txtName = root.findViewById(R.id.txtName);
        txtDate = root.findViewById(R.id.txtDate);
        txtVaccineType = root.findViewById(R.id.txtVaccineType);
        txtVaccineDose = root.findViewById(R.id.txtVaccineDose);
        txtFrontImage = root.findViewById(R.id.txtFrontImage);
        txtBackImage = root.findViewById(R.id.txtBackImage);

        btnHome = root.findViewById(R.id.fab_home);
        btnEdit = root.findViewById(R.id.fab_edit);
        btnDelete = root.findViewById(R.id.fab_delete);

        lvCameraFront = root.findViewById(R.id.lvCameraFront);
        lvCameraBack = root.findViewById(R.id.lvCameraBack);

        btnHome.setOnClickListener(v -> {
            NavHostFragment.findNavController(VaccineView.this)
                    .navigate(R.id.action_VaccineViewFragment_to_FirstFragment);
        });

        showVaccineRecord();


        return root;
    }

    void showVaccineRecord(){

        vaccineRecord = GlobalContainer.VaccinationToView;
        txtName.setText(vaccineRecord.Name);
        txtDate.setText(DateHelper.DateToString(vaccineRecord.VaccineDate));
        txtVaccineType.setText(vaccineRecord.VaccineName);
        txtVaccineDose.setText(vaccineRecord.VaccineDose);

        if (vaccineRecord.FrontImagePath == null || vaccineRecord.FrontImagePath.isEmpty() == true){
            lvCameraFront.setBackgroundColor(Color.DKGRAY);
            txtFrontImage.setText("No Front Image");
        }
        else
        {
            lvCameraFront.setBackgroundColor(Color.GREEN) ;
        }

        if (vaccineRecord.BackImagePath == null || vaccineRecord.BackImagePath.isEmpty() == true){
            lvCameraBack.setBackgroundColor(Color.DKGRAY);
            txtBackImage.setText("No Back Image");
        }
        else
        {
            lvCameraBack.setBackgroundColor(Color.GREEN) ;
        }

    }
}