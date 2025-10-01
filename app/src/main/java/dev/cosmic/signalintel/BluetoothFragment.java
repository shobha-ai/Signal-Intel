package dev.cosmic.signalintel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private final ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private android.bluetooth.BluetoothAdapter bluetoothAdapter; // Correctly referencing the system class
    private BluetoothAdapter recyclerAdapter; // Our custom adapter

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getPairedDevices();
                } else {
                    Toast.makeText(getContext(), "Permission denied. Bluetooth is unavailable.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothManager bluetoothManager = requireContext().getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.bluetooth_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        recyclerAdapter = new BluetoothAdapter(deviceList); // Using the correct variable name and constructor
        recyclerView.setAdapter(recyclerAdapter);

        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "This device does not support Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            checkPermissionAndGetDevices();
        }

        return view;
    }

    private void checkPermissionAndGetDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                getPairedDevices();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else {
            getPairedDevices();
        }
    }

    @SuppressLint("MissingPermission")
    private void getPairedDevices() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(getContext(), "Bluetooth is not enabled.", Toast.LENGTH_SHORT).show();
            return;
        }

        deviceList.clear();
        Set<android.bluetooth.BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (android.bluetooth.BluetoothDevice device : pairedDevices) {
                deviceList.add(new BluetoothDevice(device.getName(), device.getAddress()));
            }
        }
        recyclerAdapter.notifyDataSetChanged(); // Correctly calling the method on our adapter
    }
}
