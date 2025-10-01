package dev.cosmic.signalintel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line connects the Java code to our activity_main.xml layout.
        setContentView(R.layout.activity_main);

        // Find the UI elements we defined in the XML layout by their IDs.
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // Set the toolbar as the app's action bar.
        setSupportActionBar(toolbar);

        // Create an instance of our custom adapter.
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        // Set the adapter on the ViewPager2.
        viewPager.setAdapter(adapter);

        // This is the magic that links the tabs to the ViewPager.
        // It automatically handles tab clicks and swipe gestures.
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 1) {
                tab.setText("Bluetooth");
            } else {
                tab.setText("Wi-Fi");
            }
        }).attach();
    }
}
