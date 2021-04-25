package com.bstharun.covidshot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VaccinationAdapter extends   RecyclerView.Adapter<VaccinationAdapter.ViewHolder> {

    private ArrayList<Vaccination> vaccinations;

    public VaccinationAdapter(ArrayList<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_vaccination, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Vaccination vaccination = vaccinations.get(position);

        // Set item views based on your views and data model

        holder.txtName.setText(vaccination.Name);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        String date = sdf.format(vaccination.Date);
        holder.txtDate.setText(date);

        holder.txtShotType.setText(vaccination.Type);
    }

    @Override
    public int getItemCount() {
        return vaccinations.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView txtName;
        public TextView txtDate;
        public TextView txtShotType;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtShotType = (TextView) itemView.findViewById(R.id.txtShotType);

        }

        @Override
        public void onClick(View view) {
            /*int position = getAdapterPosition();
            String selectedCategory = mCategories.get(position).Name;

            MainActivity activity = (MainActivity) this.itemView.getContext();
            activity.loadSubCategoriesFragment(selectedCategory); */
        }

    }
}
