package io.miha.btcontroller.communicator;

import java.io.IOException;

public interface BtCommunicator {

    void send(char c) throws IOException;

    byte[] read() throws IOException;
}
