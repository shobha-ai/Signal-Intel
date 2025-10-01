package dev.cosmic.signalintel;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MacVendorLookup {

    private static final Map<String, String> VENDOR_MAP = new HashMap<>();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());

    static {
        // Our local cache for common or known vendors
        VENDOR_MAP.put("DC:A6:32", "Google, Inc.");
        VENDOR_MAP.put("3C:5A:B4", "Google, Inc.");
        VENDOR_MAP.put("9C:A2:F4", "TP-LINK");
        VENDOR_MAP.put("CC:40:D0", "TP-LINK");
        VENDOR_MAP.put("D8:FB:5E", "Netgear");
    }

    public static void findVendor(String bssid, TextView targetTextView) {
        if (bssid == null || bssid.length() < 8) {
            targetTextView.setText("Unknown");
            return;
        }

        String prefix = bssid.substring(0, 8).toUpperCase(Locale.ROOT);
        
        if (VENDOR_MAP.containsKey(prefix)) {
            targetTextView.setText(VENDOR_MAP.get(prefix));
            return;
        }

        targetTextView.setText("Looking up...");
        executor.execute(() -> {
            String result = onlineLookup(bssid);
            handler.post(() -> targetTextView.setText(result));
        });
    }

    private static String onlineLookup(String bssid) {
        try {
            // This is the corrected, public API endpoint.
            URL url = new URL("https://api.macaddress.io/v1?output=vendor&search=" + bssid);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String vendor = in.readLine();
                in.close();
                // Cache the new result for future lookups.
                if (vendor != null && !vendor.isEmpty()) {
                    String prefix = bssid.substring(0, 8).toUpperCase(Locale.ROOT);
                    VENDOR_MAP.put(prefix, vendor);
                }
                return vendor;
            } else if (responseCode == 404) { // Not Found
                return "Unknown";
            } else {
                return "Lookup Error";
            }
        } catch (Exception e) {
            return "N/A";
        }
    }
}
