package dev.cosmic.signalintel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {

    private final List<WifiNetwork> networkList;

    public WifiAdapter(List<WifiNetwork> networkList) {
        this.networkList = networkList;
    }

    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wifi, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder holder, int position) {
        WifiNetwork network = networkList.get(position);
        Context context = holder.itemView.getContext();

        // --- Populate the new UI fields ---
        holder.networkName.setText(network.getSsid());
        holder.macAddress.setText(network.getBssid().toUpperCase(Locale.ROOT));
        holder.signalStrengthText.setText(String.format(Locale.US, "%d dBm", network.getSignalStrength()));

        // --- Set Icons ---
        holder.signalStrengthIcon.setImageDrawable(
            ContextCompat.getDrawable(context, getSignalLevelDrawable(network.getSignalStrength()))
        );

        // --- Set Advanced Info ---
        int channel = getChannelFromFrequency(network.getFrequency());
        String band = getBandFromFrequency(network.getFrequency());
        holder.frequencyValue.setText(String.format(Locale.US, "%s / %d", band, channel));
        holder.securityValue.setText(getSecurityString(network.getCapabilities()));
        
        // --- Placeholder for Manufacturer (Future Feature) ---
        holder.manufacturerValue.setText("Unknown");
    }

    @Override
    public int getItemCount() {
        return networkList.size();
    }

    // --- Helper Methods for Data Conversion ---

    private int getSignalLevelDrawable(int rssi) {
        if (rssi > -55) return R.drawable.ic_wifi_strength_4; // Excellent
        if (rssi > -67) return R.drawable.ic_wifi_strength_3; // Good
        if (rssi > -78) return R.drawable.ic_wifi_strength_2; // Fair
        return R.drawable.ic_wifi_strength_1; // Poor
    }
    
    private String getBandFromFrequency(int frequency) {
        if (frequency >= 2400 && frequency < 3000) {
            return "2.4 GHz";
        } else if (frequency >= 5000 && frequency < 6000) {
            return "5 GHz";
        }
        return "?";
    }

    private int getChannelFromFrequency(int frequency) {
        if (frequency >= 2412 && frequency <= 2484) {
            return (frequency - 2412) / 5 + 1;
        } else if (frequency >= 5180 && frequency <= 5825) {
            return (frequency - 5180) / 5 + 36;
        }
        return -1;
    }

    private String getSecurityString(String capabilities) {
        final String capLower = capabilities.toLowerCase();
        if (capLower.contains("wpa3")) {
            return "WPA3";
        } else if (capLower.contains("wpa2")) {
            return "WPA2";
        } else if (capLower.contains("wpa")) {
            return "WPA";
        } else if (capLower.contains("wep")) {
            return "WEP";
        } else {
            return "Open";
        }
    }

    // --- ViewHolder ---
    // This inner class now finds all the new TextViews in our layout.
    public static class WifiViewHolder extends RecyclerView.ViewHolder {
        public final TextView networkName;
        public final TextView macAddress;
        public final TextView signalStrengthText;
        public final ImageView signalStrengthIcon;
        public final TextView frequencyValue;
        public final TextView manufacturerValue;
        public final TextView securityValue;

        public WifiViewHolder(@NonNull View itemView) {
            super(itemView);
            networkName = itemView.findViewById(R.id.network_name);
            macAddress = itemView.findViewById(R.id.mac_address);
            signalStrengthText = itemView.findViewById(R.id.signal_strength_text);
            signalStrengthIcon = itemView.findViewById(R.id.signal_strength_icon);
            frequencyValue = itemView.findViewById(R.id.frequency_value);
            manufacturerValue = itemView.findViewById(R.id.manufacturer_value);
            securityValue = itemView.findViewById(R.id.security_value);
        }
    }
}
