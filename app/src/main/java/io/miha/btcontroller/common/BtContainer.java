package io.miha.btcontroller.common;

import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BtContainer {

    private Map<BtDevice, BluetoothSocket> connectedDevices = new HashMap<>();
    private Set<BtDevice> discoveredDevices = new HashSet<>();

    public void addConnected(BtDevice btDevice, BluetoothSocket socket) {
        connectedDevices.put(btDevice, socket);
    }

    public BluetoothSocket getSocketForDevice(BtDevice btDevice) {
        return connectedDevices.get(btDevice);
    }

    public List<BtDevice> getConnected() {
        return new ArrayList<>(connectedDevices.keySet());
    }

    public void removeConnected(BtDevice btDevice) {
        connectedDevices.remove(btDevice);
    }

    public List<BtDevice> getDiscovered() {
        return new ArrayList<>(discoveredDevices);
    }

    public void addDiscovered(BtDevice btDevice) {
        discoveredDevices.add(btDevice);
    }

    public void clearDiscovered() {
        discoveredDevices.clear();
    }
}
