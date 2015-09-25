package io.miha.btcontroller.connector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import io.miha.btcontroller.communicator.BtCommunicationActivity;

public class SimpleDialogFactory implements DialogFactory{

    @Override
    public Dialog createConnectDialog(Context context, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.connect_message);
        builder.setPositiveButton("OK", positiveClickListener);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public Dialog createDisconnectDialog(Context context, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.disconnect_message);
        builder.setPositiveButton("OK", positiveClickListener);
        builder.setNegativeButton("Cancel", negativeClickListener);
        return builder.create();
    }


}

