package dev.cosmic.signalintel;

// This class now holds all the advanced data for a single Wi-Fi network.
public class WifiNetwork {

    private final String ssid;
    private final String bssid; // MAC Address
    private final int signalStrength;
    private final int frequency;
    private final String capabilities; // Security info

    public WifiNetwork(String ssid, String bssid, int signalStrength, int frequency, String capabilities) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.signalStrength = signalStrength;
        this.frequency = frequency;
        this.capabilities = capabilities;
    }

    // "Getter" methods to allow other classes to read the data.
    public String getSsid() { return ssid; }
    public String getBssid() { return bssid; }
    public int getSignalStrength() { return signalStrength; }
    public int getFrequency() { return frequency; }
    public String getCapabilities() { return capabilities; }
}
