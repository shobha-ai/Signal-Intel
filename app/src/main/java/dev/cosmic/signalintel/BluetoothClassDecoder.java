package dev.cosmic.signalintel;

import android.bluetooth.BluetoothClass;

// This is a helper class to decode the integer "Bluetooth Class"
// into a human-readable device type.
public class BluetoothClassDecoder {

    public static String getDeviceType(int bluetoothClass) {
        switch (getMajorDeviceClass(bluetoothClass)) {
            case BluetoothClass.Device.Major.COMPUTER:
                return "Computer";
            case Bluetooth-Class.Device.Major.PHONE:
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
                return getWearableDeviceType(bluetoothClass);
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
        return bluetoothClass & BluetoothClass.Device.Major.CLASS_MASK;
    }
    
    private static String getAudioVideoDeviceType(int bluetoothClass) {
        switch (bluetoothClass) {
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                return "Wearable Headset";
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                return "Hands-free Device";
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
            case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO:
                return "HiFi Audio";

            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                return "VCR";
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                return "Video Camera";
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
                return "Camcorder";
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                return "Video Monitor";

            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                return "TV / Display";
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                return "Video Conferencing";
            default:
                return "Audio/Video Device";
        }
    }
    
    private static String getWearableDeviceType(int bluetoothClass) {
        switch (bluetoothClass) {
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                return "Smartwatch";
            case BluetoothClass.Device.WEARABLE_PAGER:
                return "Pager";
            case BluetoothClass.Device.WEARABLE_JACKET:
                return "Jacket";
            case BluetoothClass.Device.WEARABLE_HELMET:
                return "Helmet";
            case BluetoothClass.Device.WEARABLE_GLASSES:
                return "Smart Glasses";
            default:
                return "Wearable";
        }
    }
}
