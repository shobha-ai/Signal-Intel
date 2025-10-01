package dev.cosmic.signalintel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // This method determines which fragment to show based on the tab position.
        // Position 0 is the first tab, Position 1 is the second, and so on.
        if (position == 1) {
            return new BluetoothFragment();
        }
        // Default to the WifiFragment for the first tab (position 0).
        return new WifiFragment();
    }

    @Override
    public int getItemCount() {
        // This tells the adapter that we have a total of 2 tabs.
        return 2;
    }
}
