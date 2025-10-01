package dev.cosmic.signalintel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {

    private final List<BluetoothDevice> deviceList;

    public BluetoothAdapter(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bluetooth, parent, false);
        return new BluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothViewHolder holder, int position) {
        BluetoothDevice device = deviceList.get(position);
        
        // Use a placeholder if the device name is null or empty.
        String deviceName = device.getName();
        if (deviceName == null || deviceName.isEmpty()) {
            deviceName = "Unknown Device";
        }
        holder.deviceName.setText(deviceName);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class BluetoothViewHolder extends RecyclerView.ViewHolder {
        public final TextView deviceName;
        public final ImageView bluetoothIcon;

        public BluetoothViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            bluetoothIcon = itemView.findViewById(R.id.bluetooth_icon);
        }
    }
}
