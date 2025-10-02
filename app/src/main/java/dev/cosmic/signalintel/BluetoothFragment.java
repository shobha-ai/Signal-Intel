package dev.cosmic.signalintel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {

    private final ArrayList<dev.cosmic.signalintel.BluetoothDevice> deviceList = new ArrayList<>();
    private BluetoothAdapter systemBluetoothAdapter;
    private dev.cosmic.signalintel.BluetoothAdapter recyclerAdapter;
    private boolean isScanning = false;

    private final ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                if (permissions.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false)) {
                    startDiscovery();
                } else {
                    Toast.makeText(getContext(), "Bluetooth Scan permission denied.", Toast.LENGTH_LONG).show();
                }
            });

    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    addDeviceToList(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                isScanning = false;
                Toast.makeText(getContext(), "Scan finished.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothManager bluetoothManager = requireContext().getSystemService(BluetoothManager.class);
        systemBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.bluetooth_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerAdapter = new dev.cosmic.signalintel.BluetoothAdapter(deviceList);
        recyclerView.setAdapter(recyclerAdapter);

        if (systemBluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth not supported", Toast.LENGTH_LONG).show();
        } else {
            getPairedDevices();
        }
        
        // We will add a scan button here in a future step.
        // For now, we will add a placeholder to trigger the scan.
        view.findViewById(R.id.bluetooth_recycler_view).setOnClickListener(v -> startDiscoveryWithPermissions());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        requireActivity().registerReceiver(discoveryReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(discoveryReceiver);
        if (systemBluetoothAdapter != null && isScanning) {
            stopDiscovery();
        }
    }

    @SuppressLint("MissingPermission")
    private void addDeviceToList(BluetoothDevice device) {
        // Avoid adding duplicate devices
        for (dev.cosmic.signalintel.BluetoothDevice existingDevice : deviceList) {
            if (existingDevice.getAddress().equals(device.getAddress())) {
                return;
            }
        }
        deviceList.add(new dev.cosmic.signalintel.BluetoothDevice(device.getName(), device.getAddress(), device.getBluetoothClass().getDeviceClass()));
        recyclerAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    private void getPairedDevices() {
        deviceList.clear();
        if (systemBluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = systemBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    addDeviceToList(device);
                }
            }
        } else {
            Toast.makeText(getContext(), "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void startDiscoveryWithPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissionsLauncher.launch(new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        } else {
            startDiscovery();
        }
    }

    @SuppressLint("MissingPermission")
    private void startDiscovery() {
        if (isScanning) return;
        Toast.makeText(getContext(), "Scanning for new devices...", Toast.LENGTH_SHORT).show();
        isScanning = true;
        systemBluetoothAdapter.startDiscovery();
    }
    
    @SuppressLint("MissingPermission")
    private void stopDiscovery() {
        if (isScanning) {
            systemBluetoothAdapter.cancelDiscovery();
            isScanning = false;
        }
    }
}
