package com.bstharun.covidshot;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class VaccineEntryFragment extends Fragment {

    public static final int PERMISSIONS_CAMERA_REQUEST = 0;
    public static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST = 0;

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

    EditText txtName;
    TextView txtDate;
    Spinner spinnerVaccine;
    Spinner spinnerDose;
    LinearLayout lvCameraFront;
    LinearLayout lvCameraBack;
    Button btnSave;

    String frontImageFileName;
    String backImageFileName;
    Date vaccineDate;

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

        spinnerVaccine = (Spinner) root.findViewById(R.id.spinnerVaccineType);
        spinnerDose = (Spinner) root.findViewById(R.id.spinnerVaccineDose);
        txtDate = root.findViewById(R.id.txtDate);
        lvCameraFront = root.findViewById(R.id.lvCameraFront);
        lvCameraBack = root.findViewById(R.id.lvCameraBack);
        btnSave = root.findViewById(R.id.btnSave);
        txtName = (EditText) root.findViewById(R.id.txtName);

        vaccineDate = Calendar.getInstance().getTime();
        txtDate.setText(DateHelper.TodayToString());

        setVaccineSpinner();
        setVaccineDoseSpinner();

        txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });


        txtDate.setOnClickListener(v -> {
            hideKeyboard();
            showCalendar();
        });

        lvCameraFront.setOnClickListener(v -> {
            hideKeyboard();
            if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_CAMERA_REQUEST);
            } else if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {
                frontImageFileName = "Front - " + String.valueOf(System.currentTimeMillis());
                takePicture(frontImageFileName);
            }
        });

        lvCameraBack.setOnClickListener(v -> {
            hideKeyboard();
            if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_CAMERA_REQUEST);
            } else if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {
                backImageFileName = "Back - " + String.valueOf(System.currentTimeMillis());
                takePicture(backImageFileName);
            }
        });

        btnSave.setOnClickListener(v -> {
            hideKeyboard();
            saveData();
        });


        showPhoneStoragePermission();

        // link floating action button
        FloatingActionButton fab = root.findViewById(R.id.fab_home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                NavHostFragment.findNavController(VaccineEntryFragment.this)
                        .navigate(R.id.action_VaccineEntryFragment_to_FirstFragment);

            }
        });

        return root;
    }

    private void saveData(){
        if(TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setError("Enter name");
            return;
        }

        Vaccination record = new Vaccination();
        record.Id = UUID.randomUUID().toString();
        record.Name = txtName.getText().toString().toUpperCase().trim();
        record.VaccineDate = vaccineDate;
        record.VaccineName = spinnerVaccine.getSelectedItem().toString();
        record.VaccineDose = spinnerDose.getSelectedItem().toString();
        record.FrontImagePath = frontImageFileName;
        record.BackImagePath = backImageFileName;
        record.CreatedDate = Calendar.getInstance().getTime();

        DbHelper db = new DbHelper(this.getContext());
        db.storeVaccineRecord(record);

        NavHostFragment.findNavController(VaccineEntryFragment.this)
                .navigate(R.id.action_VaccineEntryFragment_to_FirstFragment);
    }


    private void showPhoneStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "CovidShot needs access to store your vaccine record image in your phone.", Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST);
            }
        } else {
            Toast.makeText(this.getActivity(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }


    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{permissionName}, permissionRequestCode);
    }

/*    void createFile() {
        //Text of the Document
        String textToWrite = "bla bla bla";

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        File storageDir = Environment.getExternalStorageDirectory();
        Log.d("MyLog", "Storage directory is: " + storageDir);
        File file = new File(this.requireContext().getExternalFilesDir(null), "abc");

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    void takePicture(String fileName) {
        try {
            File image = new File(this.requireContext().getExternalFilesDir(null), fileName);
            Log.d("MyLog", "getAbsoluteFile: " + image.getAbsoluteFile());
            image.createNewFile();

            Uri outputFileUri = FileProvider.getUriForFile(this.getActivity(), "com.bstharun.covidshot.fileprovider", image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, PERMISSIONS_CAMERA_REQUEST);
        } catch (IOException e) {
            Log.d("MyLog", e.toString());
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.getActivity(), "Camera permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getActivity(), "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.getActivity(), "Storage permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getActivity(), "Storage permission denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.d("MyLog", "Pic saved");
        }
    }

    void showCalendar() {
        hideKeyboard();
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
                        vaccineDate = calendar.getTime();

                        txtDate.setText(DateHelper.DateToString(vaccineDate));

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

    void hideKeyboard(){
        // Check if no view has focus:
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}