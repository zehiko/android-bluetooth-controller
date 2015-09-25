package io.miha.btcontroller.connector;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import io.miha.btcontroller.common.BtDevice;
import io.miha.btcontroller.communicator.BtCommunicationActivity;

public class ActivityStarterListener implements DialogInterface.OnClickListener{

    Context context;
    BtDevice btDevice;

    public ActivityStarterListener(Context context, BtDevice btDevice) {
        this.context = context;
        this.btDevice = btDevice;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        Intent i = new Intent(context, BtCommunicationActivity.class);
        i.putExtra("btDevice", btDevice);
        context.startActivity(i);
    }
}
