package io.miha.btcontroller.connector;


import io.miha.btcontroller.common.AppProvider;
import io.miha.btcontroller.common.BtContainer;
import io.miha.btcontroller.common.BtDevice;

public class BtFactory {

    public BtConnectTask createBtConnectTask(BtDevice btDevice, BtStateListener btStateListener) {
        BtContainer btContainer = AppProvider.getBtContainer();
        BtUuidResolver uuidResolver = AppProvider.getUuidResolver();

        return new BtConnectTask(btDevice, btStateListener, uuidResolver, btContainer);
    }

    public BtBroadcastReceiver createBtBroadcastReceiver(BtControllerInterface ui) {
        BtContainer btContainer = AppProvider.getBtContainer();
        return new BtBroadcastReceiver(ui, btContainer);
    }
}
