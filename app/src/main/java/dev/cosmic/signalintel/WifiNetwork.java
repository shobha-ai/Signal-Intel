package dev.cosmic.signalintel;

// This class just holds the data for a single Wi-Fi network.
public class WifiNetwork {

    private final String ssid;
    private final int signalStrength;
    private final String securityType;

    public WifiNetwork(String ssid, int signalStrength, String securityType) {
        this.ssid = ssid;
        this.signalStrength = signalStrength;
        this.securityType = securityType;
    }

    // "Getter" methods to allow other classes to read the data.
    public String getSsid() { return ssid; }
    public int getSignalStrength() { return signalStrength; }
    public String getSecurityType() { return securityType; }
}
