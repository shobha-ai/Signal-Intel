package dev.cosmic.signalintel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// The Adapter is the bridge between our data and the RecyclerView.
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {

    private final List<WifiNetwork> networkList;

    public WifiAdapter(List<WifiNetwork> networkList) {
        this.networkList = networkList;
    }

    // This is called when a new card view needs to be created.
    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wifi, parent, false);
        return new WifiViewHolder(view);
    }

    // This is called to populate a card with data for a specific position.
    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder holder, int position) {
        WifiNetwork network = networkList.get(position);
        holder.networkName.setText(network.getSsid());
        holder.signalStrengthText.setText(network.getSignalStrength() + " dBm");
        // We will add icon logic later.
    }

    // This tells the RecyclerView the total number of items in the list.
    @Override
    public int getItemCount() {
        return networkList.size();
    }

    // The ViewHolder holds the views for a single card to improve performance.
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
