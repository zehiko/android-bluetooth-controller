package io.miha.btcontroller.connector;

import java.util.List;

import io.miha.btcontroller.common.BtDevice;

public interface BtConnector {

    List<BtDevice> fetchPairedDevices();

    void discoverDevices();

    void disconnect(BtDevice btDevice);


}
