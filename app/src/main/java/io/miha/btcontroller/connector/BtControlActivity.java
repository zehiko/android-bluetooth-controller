package io.miha.btcontroller.connector;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.miha.btcontroller.common.AppProvider;
import io.miha.btcontroller.common.BtContainer;
import io.miha.btcontroller.common.BtDevice;
import io.miha.btcontroller.common.ToastNotificator;

public class BtControlActivity extends AppCompatActivity implements BtControllerInterface {

    private static final String TAG = "BtControlActivity";

    private final int REQUEST_ENABLE_BT = 1;
    ArrayAdapter<BtDevice> discoveredArrayAdapter;
    ArrayAdapter<BtDevice> connectedArrayAdater;
    ArrayAdapter<BtDevice> pairedArrayAdapter;
    private BluetoothDevice desiredDevice;
    private BtConnector btConnector;
    private boolean discoveryStarted = false;
    private boolean btEnableStarted = false;

    Button scanButton;
    private BroadcastReceiver  broadcastReceiver;

    private DialogFactory dialogFactory = new SimpleDialogFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_control);

        Log.d(TAG, "onCreate");

        scanButton = (Button) findViewById(R.id.scan_button);

        discoveredArrayAdapter = new ArrayAdapter(this, R.layout.list_item);
        connectedArrayAdater = new ArrayAdapter<BtDevice>(this, R.layout.list_item);
        pairedArrayAdapter = new ArrayAdapter<BtDevice>(this, R.layout.list_item);

        ListView connectedListView = (ListView) findViewById(R.id.connectedDevices);
        connectedListView.setAdapter(connectedArrayAdater);
        connectedListView.setOnItemClickListener(createDisconnectListener());

        ListView pairedListView = (ListView) findViewById(R.id.pairedDevices);
        pairedListView.setAdapter(pairedArrayAdapter);
        pairedListView.setOnItemClickListener(createConnectListener());

        ListView listView =  (ListView) findViewById(R.id.listView);
        listView.setAdapter(discoveredArrayAdapter);
        listView.setOnItemClickListener(createConnectListener());


        btConnector = new SimpleBtConnector(this);
        disableScanButton();

        checkBluetooth();
    }

    private void checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView tv = (TextView) findViewById(R.id.device_list);
        tv.setTextSize(40);
        if (bluetoothAdapter == null) {
            tv.setText("Device does not support Bluetooth");
        }

        Log.d(TAG, "Checking bluetooth state");
        if (!bluetoothAdapter.isEnabled()) {
            if (!btEnableStarted) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                btEnableStarted = true;
            }
        } else {
            listPreviouslyPaired();
            enableScanButton();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        discoveryStarted = false;

        checkBluetooth();

        listDiscovered();
        listConnected();

        enableScanButton();
    }


    public void startDeviceDiscovery(View view) {
        if (!discoveryStarted) {
            disableScanButton();
            Log.d(TAG, "start scanning");

            btConnector.discoverDevices();
            discoveryStarted = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            btEnableStarted = false;

            if (resultCode == RESULT_OK) {
                ToastNotificator.toastNotify(getApplicationContext(), "Bluetooth enabled");

                listPreviouslyPaired();
                enableScanButton();

            } else {
                ToastNotificator.toastNotify(getApplicationContext(), "Failed to enable bluetooth");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }


    // **** BtControllerInterface ****

    @Override
    public void registerBtReceiver(BroadcastReceiver br, IntentFilter filter) {
        if (broadcastReceiver == null) {
            registerReceiver(br, filter);
            this.broadcastReceiver = br;
        } else {
            Log.d(TAG, "broadcast receiver already registered");
        }
    }

    @Override
    public void updateKnownDeviceList() {
        List<BtDevice> discovered = AppProvider.getBtContainer().getDiscovered();

        if (discovered.size() > 0) {
            refillArrayAdapter(discovered, discoveredArrayAdapter);
        }
    }

    @Override
    public void updateConnectedList() {
        List<BtDevice> connected = AppProvider.getBtContainer().getConnected();
        connectedArrayAdater.clear();
        for(BtDevice btDevice: connected) {
            connectedArrayAdater.add(btDevice);
        }
    }

    @Override
    public void disconnectFailed(BtDevice btDevice) {
        ToastNotificator.toastNotify(getApplicationContext(), "Error while disconnecting from device " + btDevice);
    }


    /**** private helper methods ****/

    private void enableScanButton() {
        scanButton.setEnabled(true);
    }

    private void disableScanButton() {
        scanButton.setEnabled(false);
    }

    private void listConnected() {
        BtContainer btContainer = AppProvider.getBtContainer();
        List<BtDevice> devices = btContainer.getConnected();

        refillArrayAdapter(devices, connectedArrayAdater);
    }

    private void listDiscovered()  {
        BtContainer btContainer = AppProvider.getBtContainer();
        List<BtDevice> discoveredDevices = btContainer.getDiscovered();

        refillArrayAdapter(discoveredDevices, discoveredArrayAdapter);
    }

    private void listPreviouslyPaired() {
        List<BtDevice> paired = btConnector.fetchPairedDevices();
        refillArrayAdapter(paired, pairedArrayAdapter);
    }

    private void refillArrayAdapter(List<BtDevice> devices, ArrayAdapter arrayAdapter) {
        arrayAdapter.clear();;
        for (BtDevice device: devices) {
            arrayAdapter.add(device);
        }
    }


    private AdapterView.OnItemClickListener createConnectListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BtDevice btDevice = (BtDevice) parent.getAdapter().getItem(position);
                Dialog connectDialog = AppProvider.getDialogFactory().createConnectDialog(BtControlActivity.this, new ActivityStarterListener(BtControlActivity.this, btDevice), new DismissListener());
                connectDialog.show();
            }
        };
    }


    private AdapterView.OnItemClickListener createDisconnectListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BtDevice btDevice = (BtDevice) parent.getAdapter().getItem(position);

                Dialog disconnectDialog = AppProvider.getDialogFactory().createDisconnectDialog(BtControlActivity.this, new DisconnectListener(btConnector, btDevice), new DismissListener());
                disconnectDialog.show();
            }
        };
    }
}
