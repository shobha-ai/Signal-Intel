package dev.cosmic.signalintel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
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
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter recyclerAdapter;

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
        
        recyclerAdapter = new BluetoothAdapter(deviceList);
        recyclerView.setAdapter(recyclerAdapter);

        // Check if the device has Bluetooth at all.
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "This device does not support Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            checkPermissionAndGetDevices();
        }

        return view;
    }

    private void checkPermissionAndGetDevices() {
        // For modern Android (12+), BLUETOOTH_CONNECT is required to get paired device names.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                getPairedDevices();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else {
            // For older Android, the permission is granted at install time.
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
        recyclerAdapter.notifyDataSetChanged();
    }
}
