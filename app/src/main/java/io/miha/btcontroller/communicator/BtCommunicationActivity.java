package io.miha.btcontroller.communicator;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import io.miha.btcontroller.common.AppProvider;
import io.miha.btcontroller.connector.BtConnectTask;
import io.miha.btcontroller.common.BtDevice;
import io.miha.btcontroller.connector.BtStateListener;
import io.miha.btcontroller.connector.R;
import io.miha.btcontroller.common.ToastNotificator;

public class BtCommunicationActivity extends AppCompatActivity implements BtStateListener {

    private static final String TAG = "BtCommunicationActivity";

    private BtDevice btDevice;
    private BtCommunicator btCommunicator;

    Button sendButton;
    Button receiveButton;
    Button clearButton;
    EditText editText;
    TextView received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_communication);

        Intent intent = getIntent();
        this.btDevice = (BtDevice)intent.getSerializableExtra("btDevice");
        Log.d(TAG, "BT device name:"  + this.btDevice.getName());

        BtConnectTask btConnectTask = AppProvider.getBtFactory().createBtConnectTask(btDevice, this);
        btConnectTask.execute();
    }


    /*** BtStateListener methods ***/

    @Override
    public void onConnected(BluetoothSocket socket) {
        btCommunicator = new SimpleBtCommunicator(socket);
        viewOnConnected();
    }

	@Override
	public void onConnectFailed(BtDevice btDevice) {
        ToastNotificator.toastNotify(getApplicationContext(), "Failed to connect to " + btDevice.getName());
    }

    @Override
    public void onMissingUuid(BtDevice btDevice) {
        //TODO: show dialog and enter uuid if needed
        ToastNotificator.toastNotify(getApplicationContext(), "No UUID found for device:" + btDevice.getName());

    }

    /*** View handling methods ***/

    void viewOnConnected() {
        TextView tv = (TextView)findViewById(R.id.state_tv);
        tv.setText("Connected");

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setEnabled(true);

        receiveButton = (Button) findViewById(R.id.receive_button);
        receiveButton.setEnabled(true);

        clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setEnabled(true);

        editText = (EditText) findViewById(R.id.bt_input);

        received = (TextView) findViewById(R.id.receive_tv);
    }


    public void send(View view) {
        Editable text = editText.getText();
        if (text.length() > 0) {
            for (int i = 0; i < text.length(); i++) {
                try {
                    btCommunicator.send(text.charAt(i));
                } catch (IOException e) {
                    ToastNotificator.toastNotify(getApplicationContext(), getResources().getString(R.string.sending_failed));
                }
            }
        } else {
           ToastNotificator.toastNotify(getApplicationContext(), getResources().getString(R.string.no_input));
        }
    }

    public void receive(View view) {
        try {
            byte[] data = btCommunicator.read();
            if (data.length != 0) {
                received.setText(new String(data));
            }

        } catch (IOException e) {
            ToastNotificator.toastNotify(getApplicationContext(), "Reading failed");
        }
    }

    public void clear(View view) {
        received.setText("");
    }
}
