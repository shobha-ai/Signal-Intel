package dev.cosmic.signalintel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BluetoothFragment extends Fragment {

    // This method connects this fragment's Java code to its XML layout.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This line loads the fragment_bluetooth.xml file.
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }
}
