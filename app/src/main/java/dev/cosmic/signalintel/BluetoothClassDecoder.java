package dev.cosmic.signalintel;

import android.bluetooth.BluetoothClass;

public class BluetoothClassDecoder {

    public static String getDeviceType(int bluetoothClass) {
        switch (getMajorDeviceClass(bluetoothClass)) {
            case BluetoothClass.Device.Major.COMPUTER:
                return "Computer";
            case BluetoothClass.Device.Major.PHONE:
                return "Phone";
            case BluetoothClass.Device.Major.NETWORKING:
                return "Network Device";
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return getAudioVideoDeviceType(bluetoothClass);
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "Peripheral";
            case BluetoothClass.Device.Major.IMAGING:
                return "Imaging Device";
            case BluetoothClass.Device.Major.WEARABLE:
                return "Wearable"; // Simplified for now
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

    private static int getMajorDeviceClass(int bluetoothClass) {
        // This is the corrected line.
        return bluetoothClass & BluetoothClass.Device.CLASS_MASK;
    }

    private static String getAudioVideoDeviceType(int bluetoothClass) {
        switch (bluetoothClass) {
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                return "Wearable Headset";
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                return "Hands-free";
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                return "Loudspeaker";
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
                return "Headphones";
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                return "Portable Audio";
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                return "Car Audio";
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                return "Set-top Box";
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                return "TV / Display";
            default:
                return "Audio/Video";
        }
    }
}
