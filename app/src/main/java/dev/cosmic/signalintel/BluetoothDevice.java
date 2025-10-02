package dev.cosmic.signalintel;

public class BluetoothDevice {

    private final String name;
    private final String address;
    private final int deviceClass; // New field for device type

    public BluetoothDevice(String name, String address, int deviceClass) {
        this.name = name;
        this.address = address;
        this.deviceClass = deviceClass;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    
    public int getDeviceClass() {
        return deviceClass;
    }
}
