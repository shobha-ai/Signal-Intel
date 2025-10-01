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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WifiFragment extends Fragment {

    private WifiAdapter adapter;
    private WifiManager wifiManager;
    private FloatingActionButton fabScan;
    private final ArrayList<WifiNetwork> networkList = new ArrayList<>();
    private boolean isScanning = false;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startWifiScan();
                } else {
                    Toast.makeText(getContext(), "Permission denied. Wi-Fi scanning is unavailable.", Toast.LENGTH_LONG).show();
                }
            });

    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                stopScanAnimation();
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    scanFailure("Scan results not updated.");
                }
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wifi_recycler_view);
        fabScan = view.findViewById(R.id.fab_scan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WifiAdapter(networkList);
        recyclerView.setAdapter(adapter);

        fabScan.setOnClickListener(v -> handleScanButtonClick());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        requireActivity().registerReceiver(wifiScanReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(wifiScanReceiver);
        if (isScanning) {
            stopScanAnimation();
        }
    }

    private void handleScanButtonClick() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startWifiScan();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startWifiScan() {
        if (isScanning) {
            return;
        }
        startScanAnimation();
        boolean success = wifiManager.startScan();
        if (!success) {
            stopScanAnimation();
            scanFailure("Wi-Fi scan failed to start.");
        }
    }

    private void scanSuccess() {
        if (getContext() == null) return;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<ScanResult> results = wifiManager.getScanResults();
            networkList.clear();
            for (ScanResult scanResult : results) {
                if (scanResult.SSID != null && !scanResult.SSID.isEmpty()) {
                    // This is the key change: we now pass ALL the advanced data to our WifiNetwork object.
                    networkList.add(new WifiNetwork(
                            scanResult.SSID,
                            scanResult.BSSID,
                            scanResult.level,
                            scanResult.frequency,
                            scanResult.capabilities
                    ));
                }
            }
            Collections.sort(networkList, Comparator.comparingInt(WifiNetwork::getSignalStrength).reversed());
            adapter.notifyDataSetChanged();
        }
    }

    private void scanFailure(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void startScanAnimation() {
        isScanning = true;
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        fabScan.startAnimation(rotate);
    }

    private void stopScanAnimation() {
        isScanning = false;
        if (fabScan != null) {
            fabScan.clearAnimation();
        }
    }
}
