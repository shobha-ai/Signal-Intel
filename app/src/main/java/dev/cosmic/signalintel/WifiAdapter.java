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

        holder.networkName.setText(network.getSsid());
        holder.signalStrengthText.setText(network.getSignalStrength() + " dBm");

        // --- ICON LOGIC ---

        // Set the signal strength icon based on the RSSI level.
        int signalLevel = getSignalLevel(network.getSignalStrength());
        if (signalLevel >= 4) {
            holder.wifiIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wifi_strength_4));
        } else if (signalLevel == 3) {
            holder.wifiIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wifi_strength_3));
        } else if (signalLevel == 2) {
            holder.wifiIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wifi_strength_2));
        } else {
            holder.wifiIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wifi_strength_1));
        }

        // Set the security icon based on the network capabilities string.
        if (isNetworkOpen(network.getSecurityType())) {
            holder.securityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_no_encryption));
        } else {
            holder.securityIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock));
        }
    }

    @Override
    public int getItemCount() {
        return networkList.size();
    }

    // Helper method to convert dBm to a simple 0-4 level.
    private int getSignalLevel(int rssi) {
        if (rssi > -55) return 4; // Excellent
        if (rssi > -67) return 3; // Good
        if (rssi > -78) return 2; // Fair
        return 1; // Poor
    }
    
    // Helper method to check if the network is open or secured.
    private boolean isNetworkOpen(String capabilities) {
        final String capabilitiesLower = capabilities.toLowerCase();
        return !capabilitiesLower.contains("wpa") && !capabilitiesLower.contains("wep");
    }

    public static class WifiViewHolder extends RecyclerView.ViewHolder {
        public final TextView networkName;
        public final TextView signalStrengthText;
        public final ImageView wifiIcon;
        public final ImageView securityIcon;

        public WifiViewHolder(@NonNull View itemView) {
            super(itemView);
            networkName = itemView.findViewById(R.id.network_name);
            signalStrengthText = itemView.findViewById(R.id.signal_strength_text);
            wifiIcon = itemView.findViewById(R.id.wifi_icon);
            securityIcon = itemView.findViewById(R.id.security_icon);
        }
    }
}
