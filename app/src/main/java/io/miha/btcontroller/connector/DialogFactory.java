package io.miha.btcontroller.connector;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public interface DialogFactory {

    Dialog createConnectDialog(Context context, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener);

    Dialog createDisconnectDialog(Context context, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener);
}
