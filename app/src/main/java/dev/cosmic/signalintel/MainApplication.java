package dev.cosmic.signalintel;

import androidx.multidex.MultiDexApplication;

// This class ensures that the MultiDex library is initialized correctly
// before any other application code runs.
public class MainApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // Other app-wide initializations can be placed here in the future.
    }
}
