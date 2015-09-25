package io.miha.btcontroller.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

import io.miha.btcontroller.common.BtContainer;
import io.miha.btcontroller.common.BtDevice;

public class BtConnectTask extends AsyncTask<Void, Integer, BluetoothSocket>{

    private static final int NO_UUID = 0;
    private static final int CONN_FAILED = 1;


    private BtStateListener btStateListener;
    private BtDevice btDevice;

  	private BtUuidResolver uuidResolver;
	private BtContainer btContainer;

    public BtConnectTask(BtDevice btDevice,  BtStateListener btStateListener, BtUuidResolver uuidResolver, BtContainer btContainer) {
        this.btDevice = btDevice;
        this.btStateListener = btStateListener;
		this.btContainer = btContainer;
		this.uuidResolver = uuidResolver;
    }

    @Override
    protected BluetoothSocket doInBackground(Void... params) {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.cancelDiscovery();
        BluetoothDevice device = adapter.getRemoteDevice(btDevice.getMac());

        try {
			UUID deviceUuid = uuidResolver.uuidForName(btDevice.getName());
			if (deviceUuid == null) {
				publishProgress(NO_UUID);
				return null;
			} else {
        	    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(deviceUuid);
    	        socket.connect();

	            return socket;
			}
        } catch (IOException e) {
			publishProgress(CONN_FAILED);
        }
        return null;
    }

	@Override
	protected void onProgressUpdate(Integer... progress) {
        if (progress[0] == NO_UUID) {
            btStateListener.onMissingUuid(btDevice);

        } else if (progress[0] == CONN_FAILED) {
            btStateListener.onConnectFailed(btDevice);
        }
	}

    @Override
    protected void onPostExecute(BluetoothSocket bluetoothSocket) {
        if (bluetoothSocket != null) {
            btContainer.addConnected(btDevice, bluetoothSocket);
            btStateListener.onConnected(bluetoothSocket);
        }
    }
}
