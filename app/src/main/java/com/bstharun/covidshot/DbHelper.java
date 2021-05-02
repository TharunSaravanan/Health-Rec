package com.bstharun.covidshot;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DbHelper {
    Context context;

    public DbHelper(Context context) {
        this.context = context;
    }

    public void storeVaccineRecord(Vaccination vaccineEntry) {

        // get all vaccinations
        // check if the vaccineEntry id is already present with existing vaccinations
        // if vaccine entry is not available, it is a new vaccine entry - just add to the vaccinations
        // if vaccine entry is available, user has edited existing vaccine entry. in that case, delete existing in the arraylist and add the edited entry
        // save all vaccinations

        ArrayList<Vaccination> vaccinations = getAllVaccinations();
        vaccinations.add(vaccineEntry);
        saveAllVaccinations(vaccinations);
    }

    public ArrayList<Vaccination> getAllVaccinations() {
        ArrayList<Vaccination> allVaccines = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("com.bstharun.covidshot", Context.MODE_PRIVATE);
        prefs.getBoolean("keystring", true);

        String json = prefs.getString("all-vaccine-records", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();

            Type listOfMyClassObject = new TypeToken<ArrayList<Vaccination>>() {
            }.getType();
            allVaccines = gson.fromJson(json, listOfMyClassObject);
        }

        return allVaccines;
    }

    public void saveAllVaccinations(ArrayList<Vaccination> allVaccinations) {
        SharedPreferences prefs = context.getSharedPreferences("com.bstharun.covidshot", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(allVaccinations);

        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.putString("all-vaccine-records", json);
        edit.commit();
    }
}
