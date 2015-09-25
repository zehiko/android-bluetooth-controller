package io.miha.btcontroller.communicator;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SimpleBtCommunicator implements  BtCommunicator{

    private static final String TAG = "SimpleBtCommunicator";

    private BluetoothSocket socket;
    private static final int EoS = -1;

    public SimpleBtCommunicator(BluetoothSocket socket) {
        this.socket = socket;
    }


    @Override
    public void send(char c) throws  IOException {
        Log.d(TAG, "sending " + c);
        OutputStream btOutputStream = socket.getOutputStream();
        btOutputStream.write(c);
    }

    @Override
    public byte[] read() throws IOException {
        InputStream btInputStream = socket.getInputStream();
        int bytesAvailable = btInputStream.available();
        Log.d(TAG, "data received: "  + bytesAvailable);
        if (bytesAvailable > 0) {
            byte[] data = new byte[bytesAvailable];
            btInputStream.read(data);
            return data;
        } else {
            return new byte[0];
        }
    }
}
