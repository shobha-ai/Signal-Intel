package dev.cosmic.signalintel;

import android.bluetooth.BluetoothClass;

public class BluetoothClassDecoder {

    public static String getDeviceType(int bluetoothDeviceClass) {
        switch (bluetoothDeviceClass & BluetoothClass.Device.Major.CLASS_MASK) {
            case BluetoothClass.Device.Major.COMPUTER:
                return "Computer";
            case BluetoothClass.Device.Major.PHONE:
                return "Phone";
            case BluetoothClass.Device.Major.NETWORKING:
                return "Network";
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "Audio/Video";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "Peripheral";
            case BluetoothClass.Device.Major.IMAGING:
                return "Imaging";
            case BluetoothClass.Device.Major.WEARABLE:
                return "Wearable";
            case BluetoothClass.Device.Major.TOY:
                return "Toy";
            case BluetoothClass.Device.Major.HEALTH:
                return "Health Device";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "Uncategorized";
            default:
                return "Unknown";
        }
    }
}
