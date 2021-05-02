package com.bstharun.covidshot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvVaccinations = (RecyclerView) root.findViewById(R.id.rvVaccinations);

        // Read data from database
        ArrayList<Vaccination> vaccinations = new DbHelper(this.getContext()).getAllVaccinations();


        /* ArrayList<Vaccination> vaccinations = new ArrayList<>();
        Vaccination dose1 = new Vaccination();
        dose1.Name = "Tharun Saravanan";
        dose1.VaccineDate = new Date(2021, 04, 23);
        dose1.VaccineName = "Pfizer Dose 1";

        Vaccination dose2 = new Vaccination();
        dose2.Name = "Tharun Saravanan";
        dose2.VaccineDate = new Date(2021, 04, 23);
        dose2.VaccineName = "Pfizer Dose 2";

        vaccinations.add(dose1);
        vaccinations.add(dose2);*/

        // Create adapter passing in the sample user data
        VaccinationAdapter adapter = new VaccinationAdapter(vaccinations);

        // Attach the adapter to the recyclerview to populate items
        rvVaccinations.setAdapter(adapter);

        // Set layout manager to position the items
        rvVaccinations.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        // link floating action button
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_VaccineEntryFragment);

            }
        });

        return root;
    }
}