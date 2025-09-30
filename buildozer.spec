# buildozer.spec
# Configuration file for the Signal Intel Android app.

[app]
title = Signal Intel
package.name = signalintel
package.domain = com.yourname
source.dir = .
version = 0.1
requirements = python3, kivy==2.1.0
orientation = portrait
fullscreen = 0

[android]
# Permissions our app will need for Wi-Fi and Bluetooth scanning later.
android.permissions = INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, CHANGE_WIFI_STATE, BLUETOOTH, BLUETOOTH_ADMIN, ACCESS_FINE_LOCATION
android.api = 31
android.minapi = 21
android.sdk = 24
android.ndk = 25b
android.archs = arm64-v8a, armeabi-v7a
