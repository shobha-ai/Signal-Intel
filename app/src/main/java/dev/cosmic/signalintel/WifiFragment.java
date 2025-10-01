package dev.cosmic.signalintel;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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
import java.util.List;

public class WifiFragment extends Fragment {

    private WifiAdapter adapter;
    private ProgressBar progressBar;
    private WifiManager wifiManager;
    private final ArrayList<WifiNetwork> networkList = new ArrayList<>();

    // The modern permission handler.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startWifiScan();
                } else {
                    Toast.makeText(getContext(), "Permission denied. Wi-Fi scanning is unavailable.", Toast.LENGTH_LONG).show();
                }
            });

    // This is our "listener" for Wi-Fi scan results.
    // The Android system will call the onReceive method when a scan is complete.
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Get the WifiManager system service when the fragment is attached to the activity.
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wifi_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        FloatingActionButton fabScan = view.findViewById(R.id.fab_scan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize the adapter with our (currently empty) list.
        adapter = new WifiAdapter(networkList);
        recyclerView.setAdapter(adapter);

        fabScan.setOnClickListener(v -> handleScanButtonClick());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register our listener when the app becomes visible.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        requireActivity().registerReceiver(wifiScanReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister our listener when the app is no longer visible to save battery.
        requireActivity().unregisterReceiver(wifiScanReceiver);
    }

    private void handleScanButtonClick() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startWifiScan();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startWifiScan() {
        progressBar.setVisibility(View..VISIBLE);
        networkList.clear(); // Clear the old results
        adapter.notifyDataSetChanged(); // Tell the UI the list is empty

        // This is the command that tells the Android system to start a new Wi-Fi scan.
        boolean success = wifiManager.startScan();
        if (!success) {
            // The scan failed to start for some reason.
            scanFailure();
        }
    }

    private void scanSuccess() {
        progressBar.setVisibility(View.GONE);
        // We have new results. Let's process them.
        try {
            // The ACCESS_FINE_LOCATION check is required by the system, even though we just checked it.
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                List<ScanResult> results = wifiManager.getScanResults();
                networkList.clear(); // Clear the list one more time to be safe.
                for (ScanResult scanResult : results) {
                    if (scanResult.SSID != null && !scanResult.SSID.isEmpty()) {
                        // Create a WifiNetwork object from the raw scan data and add it to our list.
                        networkList.add(new WifiNetwork(scanResult.SSID, scanResult.level, scanResult.capabilities));
                    }
                }
                // Tell the adapter that the data has changed, so the UI can update.
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error processing scan results.", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanFailure() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Wi-Fi scan failed to start.", Toast.LENGTH_SHORT).show();
    }
}
