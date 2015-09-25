package io.miha.btcontroller.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.miha.btcontroller.common.AppProvider;
import io.miha.btcontroller.common.BtContainer;
import io.miha.btcontroller.common.BtDevice;

public class SimpleBtConnector implements  BtConnector{

    private BluetoothAdapter bluetoothAdapter;
    private BtControllerInterface btControllerInterface;


    public SimpleBtConnector(BtControllerInterface btControllerInterface) {
        this.btControllerInterface = btControllerInterface;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public List<BtDevice> fetchPairedDevices() {
        List<BtDevice> devices = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device: pairedDevices) {
            devices.add(new BtDevice(device.getName(), device.getAddress()));
        }

        return devices;
    }

    @Override
    public void discoverDevices() {
        bluetoothAdapter.cancelDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BtBroadcastReceiver receiver = AppProvider.getBtFactory().createBtBroadcastReceiver(btControllerInterface);
        btControllerInterface.registerBtReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void disconnect(BtDevice btDevice) {
        BtContainer btContainer = AppProvider.getBtContainer();

        BluetoothSocket socket = btContainer.getSocketForDevice(btDevice);
        if (socket != null) {
            try {
                InputStream is = socket.getInputStream();

                OutputStream os = socket.getOutputStream();

                if (is != null) {
                    is.close();
                }

                if (os != null) {
                    os.close();
                }

                socket.close();
            } catch (IOException e) {
                btControllerInterface.disconnectFailed(btDevice);
            }

        }

        btContainer.removeConnected(btDevice);
        btControllerInterface.updateConnectedList();
    }


}
