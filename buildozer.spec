# buildozer.spec
# Configuration file for the Signal Intel Android app.
# V2: Added auto-license acceptance and pinned build-tools version.

[app]
title = Signal Intel
package.name = signalintel
package.domain = com.yourname
source.dir = .
version = 0.1
requirements = python3, kivy==2.1.0
orientation = portrait
fullscreen = 0
icon.filename = %(source.dir)s/icon.png

[android]
# Permissions our app will need for Wi-Fi and Bluetooth scanning later.
# ACCESS_FINE_LOCATION is required for Wi-Fi scanning on modern Android.
android.permissions = INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, CHANGE_WIFI_STATE, BLUETOOTH, BLUETOOTH_ADMIN, ACCESS_FINE_LOCATION

# Automatically accept the SDK license agreement. THIS IS THE KEY FIX.
android.accept_sdk_license = True

# Pin the build tools to a known stable version.
android.build_tools = 34.0.0

# Modern, stable API and NDK versions to avoid common build errors.
android.api = 31
android.minapi = 21
android.sdk = 24
android.ndk = 25b

# Specify architectures for wide compatibility with modern phones.
android.archs = arm64-v8a

# Javac options to prevent potential build failures with newer Java versions.
javac.target = 1.8
javac.source = 1.8
