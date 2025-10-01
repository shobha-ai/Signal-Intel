package dev.cosmic.signalintel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WifiFragment extends Fragment {

    // This method is called by Android when the fragment needs to show its UI.
    // It connects our Java code to our XML layout.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This line loads the fragment_wifi.xml file and returns it as the fragment's view.
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }
}
