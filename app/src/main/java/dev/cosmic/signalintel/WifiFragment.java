package dev.cosmic.signalintel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WifiFragment extends Fragment {

    private WifiAdapter adapter;
    private ProgressBar progressBar;

    // This is the modern way to handle permission requests.
    // We register a callback for the permission result.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action.
                    startWifiScan();
                } else {
                    // Explain to the user that the feature is unavailable.
                    Toast.makeText(getContext(), "Permission denied. Wi-Fi scanning is unavailable.", Toast.LENGTH_LONG).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wifi_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        FloatingActionButton fabScan = view.findViewById(R.id.fab_scan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Start with an empty list.
        adapter = new WifiAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Set up the click listener for our scan button.
        fabScan.setOnClickListener(v -> handleScanButtonClick());

        return view;
    }

    private void handleScanButtonClick() {
        // Check if the app already has the required permission.
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You have permission, so you can start the scan.
            startWifiScan();
        } else {
            // You don't have permission, so you request it.
            // The result will be handled by the callback we registered above.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startWifiScan() {
        // This is where the actual scanning logic will go.
        // For now, we'll just show the progress bar and a message.
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Scanning for Wi-Fi networks...", Toast.LENGTH_SHORT).show();

        // In the next step, we will replace this with real scanning code.
    }
}
