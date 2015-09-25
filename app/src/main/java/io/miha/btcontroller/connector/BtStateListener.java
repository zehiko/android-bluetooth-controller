package io.miha.btcontroller.connector;


import android.bluetooth.BluetoothSocket;

import io.miha.btcontroller.common.BtDevice;

public interface BtStateListener {

    void onConnected(BluetoothSocket socket);
	
	void onConnectFailed(BtDevice btDevice);

    void onMissingUuid(BtDevice btDevice);
}
