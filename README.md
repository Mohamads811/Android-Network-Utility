Android-Network-Utility
=======================

A "drag and drop" utility class for Android, that can detect the device's internet connection status, Wifi/mobile network name, and network gateway.
Example project included, showcasing full usage of the class as well as realtime detection of internet connectivity status changes, switching the device's module on/off, and loading the wifi network part of the device settings.

Usage
=====

getConnectionStatus - Returns the connection state.
0 - Not connected.
1 - Wifi.
2 - Mobile data plan.

getConnectionStatusString - Does the same as getConnectionStatus, but returns the result in human-readable String format.

getWifiName - returns the name of the current Wifi network, or an empty String if unknown.

getMobileNetworkName - returns the name of the current mobile network, or an empty String if unknown.

getGateway - returns the network gateway.

If you wish to read more and see some example code, you can find an example Android project in the "Example" directory.

Enjoy! :)