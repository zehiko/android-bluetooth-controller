package io.miha.btcontroller.common;

import java.io.Serializable;

public class BtDevice implements Serializable {

    private String name;
    private String mac;

    public BtDevice(String name, String mac) {
        this.mac = mac;
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "\n" + mac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BtDevice btDevice = (BtDevice) o;

        if (name != null ? !name.equals(btDevice.name) : btDevice.name != null) return false;
        return !(mac != null ? !mac.equals(btDevice.mac) : btDevice.mac != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        return result;
    }
}
