package io.miha.btcontroller.connector;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;

import io.miha.btcontroller.common.BtContainer;
import io.miha.btcontroller.common.BtDevice;


public class BtBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BtBroadcastReceiver";

    private BtControllerInterface btControllerInterface;
    private BtContainer btContainer;
    private Set<BtDevice> devices = new LinkedHashSet<>();

    public BtBroadcastReceiver(BtControllerInterface btControllerInterface, BtContainer btContainer) {
        this.btControllerInterface = btControllerInterface;
        this.btContainer = btContainer;

        btContainer.clearDiscovered();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d(TAG, "found new device: " + device.getName());
            BtDevice btDevice = new BtDevice(device.getName(), device.getAddress());
            btContainer.addDiscovered(btDevice);
            btControllerInterface.updateKnownDeviceList();
        }
    }
}
