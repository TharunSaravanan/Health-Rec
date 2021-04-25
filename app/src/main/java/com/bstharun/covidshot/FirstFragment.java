package com.bstharun.covidshot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

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
        //ArrayList<Vaccination> vaccinations = new DatabaseHelper(this.getContext()).getRecentEntries();

        ArrayList<Vaccination> vaccinations = new ArrayList<>();
        Vaccination dose1 = new Vaccination();
        dose1.Name = "Tharun Saravanan";
        dose1.Date = new Date(2021, 04, 23);
        dose1.Type = "Pfizer Dose 1";

        Vaccination dose2 = new Vaccination();
        dose2.Name = "Tharun Saravanan";
        dose2.Date = new Date(2021, 04, 23);
        dose2.Type = "Pfizer Dose 2";

        vaccinations.add(dose1);
        vaccinations.add(dose2);

        // Create adapter passing in the sample user data
        VaccinationAdapter adapter = new VaccinationAdapter(vaccinations);

        // Attach the adapter to the recyclerview to populate items
        rvVaccinations.setAdapter(adapter);

        // Set layout manager to position the items
        rvVaccinations.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        /* Decorate for divider
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL);
        rvVaccinations.addItemDecoration(itemDecoration);

         */

/*        ImageView manualEntry = (ImageView) root.findViewById(R.id.manualEntry);

        manualEntry.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                NavController navController = MainActivity.navController;
                navController.navigate(R.id.navigation_manual_entry);
            }
        });*/

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }
}