package io.miha.btcontroller.connector;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import io.miha.btcontroller.common.BtDevice;


public interface BtControllerInterface {

    void registerBtReceiver(BroadcastReceiver br, IntentFilter filter);

    void updateKnownDeviceList();

    void updateConnectedList();

    void disconnectFailed(BtDevice btDevice);
}
