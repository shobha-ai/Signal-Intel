package dev.cosmic.signalintel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WifiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wifi_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create sample data to show in our list for now.
        List<WifiNetwork> sampleNetworks = new ArrayList<>();
        sampleNetworks.add(new WifiNetwork("My Home Wi-Fi", -45, "WPA2"));
        sampleNetworks.add(new WifiNetwork("Coffee Shop Guest", -62, "Open"));
        sampleNetworks.add(new WifiNetwork("NeighborNet_5G", -78, "WPA2"));
        sampleNetworks.add(new WifiNetwork("OfficeLAN", -33, "WPA2"));
        sampleNetworks.add(new WifiNetwork("Another Network", -55, "WPA3"));

        // Create and set the adapter.
        WifiAdapter adapter = new WifiAdapter(sampleNetworks);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
