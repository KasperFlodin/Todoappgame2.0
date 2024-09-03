package com.example.todoappgame;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todoappgame.Models.TodoItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateTodoActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TimePicker timePickerStart, timePickerEnd;
    private TextView tvPlannedStartTime, tvPlannedEndTime, etLocation;
    private boolean isStartTimePickerVisible = false;
    private boolean isEndTimePickerVisible = false;
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize UI elements
        etLocation = findViewById(R.id.etLocation);
        Button button_getLocation = findViewById(R.id.button_getLocation);
        tvPlannedStartTime = findViewById(R.id.tvPlannedStartTime);
        tvPlannedEndTime = findViewById(R.id.tvPlannedEndTime);
        Button btnSelectStartTime = findViewById(R.id.btnSelectStartTime);
        Button btnSelectEndTime = findViewById(R.id.btnSelectEndTime);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);

        // Set TimePicker to 24-hour view
        timePickerStart.setIs24HourView(true);
        timePickerEnd.setIs24HourView(true);

        // Set initial visibility of time pickers to GONE
        timePickerStart.setVisibility(View.GONE);
        timePickerEnd.setVisibility(View.GONE);

        // Register the ActivityResultLauncher for permissions
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted
                        getLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted
                        getLocation();
                    } else {
                        // No location access granted
                        Toast.makeText(CreateTodoActivity.this, "Location permission is required to get your location", Toast.LENGTH_LONG).show();
                    }
                });

        // Set OnClickListener for button to request location permission
        button_getLocation.setOnClickListener(view -> {
            locationPermissionRequest.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        });

        // Set listener for start time button
        btnSelectStartTime.setOnClickListener(v -> {
            if (isStartTimePickerVisible) {
                timePickerStart.setVisibility(View.GONE);
                isStartTimePickerVisible = false;
            } else {
                timePickerStart.setVisibility(View.VISIBLE);
                timePickerEnd.setVisibility(View.GONE);
                isStartTimePickerVisible = true;
                isEndTimePickerVisible = false;
            }
        });

        // Set listener for end time button
        btnSelectEndTime.setOnClickListener(v -> {
            if (isEndTimePickerVisible) {
                timePickerEnd.setVisibility(View.GONE);
                isEndTimePickerVisible = false;
            } else {
                timePickerEnd.setVisibility(View.VISIBLE);
                timePickerStart.setVisibility(View.GONE);
                isEndTimePickerVisible = true;
                isStartTimePickerVisible = false;
            }
        });

        // Listener for TimePicker Start
        timePickerStart.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            String startTime = hourOfDay + ":" + String.format("%02d", minute);
            tvPlannedStartTime.setText("Planned Start: " + startTime);
        });

        // Listener for TimePicker End
        timePickerEnd.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            String endTime = hourOfDay + ":" + String.format("%02d", minute);
            tvPlannedEndTime.setText("Planned End: " + endTime);
        });

        // Submit button to send data to MainActivity
        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            // Collect data from UI elements
            String taskName = ((EditText) findViewById(R.id.editTextTask)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextDescription)).getText().toString();
            String location = etLocation.getText().toString();
            String plannedStartTime = tvPlannedStartTime.getText().toString();
            String plannedEndTime = tvPlannedEndTime.getText().toString();

            // Create TodoItem object
            TodoItem todoItem = new TodoItem();
            todoItem.setName(taskName);
            todoItem.setDescription(description);
            todoItem.setPlannedStartTime(plannedStartTime);
            todoItem.setPlannedEndTime(plannedEndTime);

            // Create an Intent to pass data to MainActivity
            Intent intent = new Intent(CreateTodoActivity.this, MainActivity.class);
            intent.putExtra("todoItem", todoItem);

            // Start MainActivity
            startActivity(intent);
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        Geocoder geocoder = new Geocoder(CreateTodoActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String cityName = addresses.get(0).getLocality();
                                etLocation.setText(cityName);
                                Toast.makeText(CreateTodoActivity.this, "City: " + cityName, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CreateTodoActivity.this, "Unable to determine city name", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateTodoActivity.this, "Geocoder failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CreateTodoActivity.this, "Location is null. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
