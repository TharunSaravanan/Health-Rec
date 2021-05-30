package com.bstharun.healthrec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

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

        btnEdit.setOnClickListener(v -> {

            GlobalContainer.VaccinationToEdit = vaccineRecord;
            NavHostFragment.findNavController(VaccineView.this)
                    .navigate(R.id.action_VaccineViewFragment_to_VaccineEntryFragment);
        });

        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this.getActivity());
            builder.setCancelable(true);
            builder.setTitle("Delete Vaccine Record");
            builder.setMessage("Are you sure you want to delete this vaccine record?");
            builder.setPositiveButton("Yes, Delete It",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    deleteVaccineRecord();
                }
            });

            builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        });

        showVaccineRecord();
        return root;
    }

    void deleteVaccineRecord(){
        DbHelper db = new DbHelper(this.getContext());
        db.deleteVaccineRecord(vaccineRecord.Id);

        NavHostFragment.findNavController(VaccineView.this)
                .navigate(R.id.action_VaccineViewFragment_to_FirstFragment);
    }

    void showVaccineRecord(){

        vaccineRecord = GlobalContainer.VaccinationToView;
        GlobalContainer.VaccinationToView = null;

        txtName.setText(vaccineRecord.Name);
        txtDate.setText(DateHelper.DateToString(vaccineRecord.VaccineDate));
        txtVaccineType.setText(vaccineRecord.VaccineName);
        txtVaccineDose.setText(vaccineRecord.VaccineDose);

        if (vaccineRecord.FrontImagePath == null || vaccineRecord.FrontImagePath.isEmpty() == true){
            lvCameraFront.setBackgroundColor(getResources().getColor(R.color.light_grey));
            txtFrontImage.setText("No Front Image");
        }
        else
        {
            lvCameraFront.setBackgroundColor(getResources().getColor(R.color.dark_green)) ;

            lvCameraFront.setOnClickListener(v -> {

                String path = this.requireContext().getExternalFilesDir(null) + "/" + vaccineRecord.FrontImagePath;
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                // rotate image with orientation
                bitmap = SetOrientation(bitmap, path);

                ImageView image = new ImageView(this.getContext());
                image.setImageBitmap(bitmap);

                AlertDialog.Builder builder=new AlertDialog.Builder(this.getActivity());
                builder.setCancelable(true);
                builder.setView(image);
                builder.setTitle("Vaccine Record Front Image");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Close",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            });
        }

        if (vaccineRecord.BackImagePath == null || vaccineRecord.BackImagePath.isEmpty() == true){
            lvCameraBack.setBackgroundColor(Color.DKGRAY);
            txtBackImage.setText("No Back Image");
        }
        else
        {
            lvCameraBack.setBackgroundColor(Color.GREEN) ;

            lvCameraBack.setOnClickListener(v -> {

                String path = this.requireContext().getExternalFilesDir(null) + "/" + vaccineRecord.BackImagePath;
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                ImageView image = new ImageView(this.getContext());
                image.setImageBitmap(bitmap);

                AlertDialog.Builder builder=new AlertDialog.Builder(this.getActivity());
                builder.setCancelable(true);
                builder.setView(image);
                builder.setTitle("Vaccine Record Back Image");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Close",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            });
        }

    }

    Bitmap SetOrientation(Bitmap source, String path){
        ExifInterface ei = null;
        Bitmap rotated = null;

        try {
            ei = new ExifInterface(path);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotated = rotateImage(source, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotated = rotateImage(source, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotated = rotateImage(source, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotated = source;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotated;
    }

    Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}