package io.miha.btcontroller.connector;

import android.content.DialogInterface;

import io.miha.btcontroller.common.BtDevice;

public class DisconnectListener implements DialogInterface.OnClickListener{

    private BtConnector btConnector;
    private BtDevice btDevice;

    public DisconnectListener(BtConnector btConnector, BtDevice btDevice) {
        this.btConnector = btConnector;
        this.btDevice = btDevice;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        btConnector.disconnect(btDevice);
        dialog.dismiss();
    }
}
