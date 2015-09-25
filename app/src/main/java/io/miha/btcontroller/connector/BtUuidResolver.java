package io.miha.btcontroller.connector;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BtUuidResolver {

    private static final String hc05_uuid = "00001101-0000-1000-8000-00805F9B34FB";

    private static final UUID hc05Uuid = UUID.fromString(hc05_uuid);

    private Map<String, UUID> uuids = new HashMap<>();

    public  BtUuidResolver() {
        uuids.put("HC-05", hc05Uuid);
        uuids.put("LT-01", hc05Uuid);
    }

    public UUID uuidForName(String deviceName) {
        return uuids.get(deviceName);
    }
}
